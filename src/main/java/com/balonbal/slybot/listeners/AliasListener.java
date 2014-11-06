package com.balonbal.slybot.listeners;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.lib.Reference;
import com.balonbal.slybot.lib.Settings;
import org.apache.commons.lang3.StringUtils;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AliasListener extends ListenerAdapter<SlyBot> {

    private static final Pattern pattern = Pattern.compile("(?<!\\\\)\\$(USER|CHANNEL|@|\\d|IF\\((.*?),(.*?),(.*?)\\)|EXEC\\((.*?)\\))");

    @Override
    public void onMessage(MessageEvent<SlyBot> event) throws Exception {
        String message = event.getMessage();
        for (String s: Reference.PREFIXES) {
            if (message.toLowerCase().startsWith(s.toLowerCase())) {
                message = message.substring(s.length());
                runAlias(message.split("\\s+"), event);
            }
        }
    }

    private void runAlias(String[] params, Event<SlyBot> event) {

        String alias = params[0].toUpperCase();
        if (Settings.aliases.containsKey(alias)) {
            String command = Settings.aliases.get(alias);

            StringBuffer buffer = new StringBuffer();
            Matcher matcher = pattern.matcher(command);

            //Search and replace
            while(matcher.find()) {
                matcher.appendReplacement(buffer, getReplacement(matcher, event, params));
            }

            matcher.appendTail(buffer);

            //Run output in both listeners
            runAlias(buffer.toString().split("\\s+"), event);
            Main.getCommandListener().getCommandHandler().processCommand(buffer.toString(), event);
        }
    }

    private String getReplacement(Matcher matcher, Event event, String[] params) {
        String s = matcher.group();

        if (s.equals("$USER")) {
            if (event instanceof MessageEvent) return ((MessageEvent) event).getUser().getNick();
            if (event instanceof PrivateMessageEvent) return ((PrivateMessageEvent) event).getUser().getNick();
        } else if (s.equals("$CHANNEL")) {
            if (event instanceof MessageEvent) return ((MessageEvent) event).getChannel().getName();
            if (event instanceof PrivateMessageEvent) return "PM";
        } else if (s.equals("$@")) {
            //Fetch the parameters excluding $0 (alias name)
            String[] newParams = new String[params.length -1];
            System.arraycopy(params, 1, newParams, 0, newParams.length);
            return StringUtils.join(newParams, " ");
        } else if (s.matches("\\$\\d")) {
            int param = Integer.parseInt(s.substring(1));
            //Check if the parameter exists
            if (param < 0 || param >= params.length) return "";
            return params[param];
        } else if (s.matches("\\$IF\\((.*?),(.*?),(.*?)\\)")) {
            //remove $ASSERT, ( and )
            s = s.substring("$IF".length() + 1, s.length() - 1);
            Matcher submatcher = pattern.matcher(s);
            StringBuffer buffer = new StringBuffer();

            //Find all expressions within the expression that needs to be replaced
            while (submatcher.find()) {
                submatcher.appendReplacement(buffer, getReplacement(submatcher, event, params));
            }

            submatcher.appendTail(buffer);

            //Split at all "," without backslashes
            String[] assessment = buffer.toString().split("(?<!\\\\),");

            //Return first parameter on successful assess, else return second
            if (assess(assessment[0].split("(?<!\\\\)(?=(==|<=|>=|<|>))|(?<=(==|<=|>=|<|>))(?<!\\\\(==|<=|>=|<|>))"))) return assessment[1];
            else return assessment[2];


        } else if (s.matches("\\$EXEC\\((.*)\\)")) {
            s = s.substring("$EXEC".length());
            Matcher submatcher = pattern.matcher(s);
            StringBuffer buffer = new StringBuffer();

            //Find and replace submatches
            while (submatcher.find()) {
                submatcher.appendReplacement(buffer, getReplacement(submatcher, event, params));
            }

            submatcher.appendTail(buffer);

            String command = buffer.toString();
            if (Main.getCommandListener().getCommandHandler().isCommand(command)) {
                //TODO Invent magic to get response from commands
            } else return "";
        }

        return "";
    }

    private boolean assess(String[] check) {
        //Remove trailing spaces
        String input = check[0].trim();
        String compare = check[2].trim();

        //TODO: Solve math?

        try {
            if (check[1].equals("==")) return input.equals(compare);

            //Do digits
            if (input.matches("\\d+") && compare.matches("\\d+")) {
                double numberA = Double.parseDouble(input);
                double numberB = Double.parseDouble(input);

                //Parse assessment
                if (check[1].equals("<=")) return numberA <= numberB;
                else if (check[1].equals(">=")) return numberA >= numberB;
                else if (check[1].equals("<")) return numberA < numberB;
                else if (check[1].equals(">")) return numberA > numberB;
            }
        } catch (Exception e) {
            System.out.println("Something went wrong while parsing input: " + e.toString());
            return false;
        }

        return false;
    }
}

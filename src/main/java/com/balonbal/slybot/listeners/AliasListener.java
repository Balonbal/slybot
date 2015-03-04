package com.balonbal.slybot.listeners;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.lib.Settings;
import org.apache.commons.lang3.StringUtils;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AliasListener extends ListenerAdapter<SlyBot> {

    private static final Pattern pattern = Pattern.compile("(?<!\\\\)\\$(USER|CHANNEL|@|\\d|#|IF\\((.*?),(.*?),(.*)\\)|EXEC\\((.*)\\))");
    private static final Pattern specialPattern = Pattern.compile("(?<!\\\\)\\$(IF|EXEC)\\((.*)\\)");

    @Override
    public void onMessage(MessageEvent<SlyBot> event) throws Exception {

        Matcher matcher = Main.getCommandListener().getTrigger().matcher(event.getMessage());
        StringBuffer buffer = new StringBuffer();

        if (matcher.find()) {
            if (matcher.start() == 0) {
                matcher.appendReplacement(buffer, "");
                String[] command = matcher.appendTail(buffer).toString().split("\\s+");
                runAlias(command, event);
            }
        }
    }

    private String runAlias(String[] params, Event<SlyBot> event) {

        String alias = params[0].toUpperCase();
        if (Settings.aliases.containsKey(alias)) {
            String command = Settings.aliases.get(alias);

            StringBuffer buffer = new StringBuffer();
            Matcher matcher = pattern.matcher(command);

            //Search and replace
            while(matcher.find()) {
                matcher.appendReplacement(buffer, getReplacement(matcher.group(), event, params));
                System.out.println(buffer.toString());
            }

            matcher.appendTail(buffer);

            String result = buffer.toString().replaceAll("\\\\", "");

            //Run output in both listeners
            if (Settings.aliases.containsKey(result.split("\\s+")[0])) return runAlias(result.split("\\s+"), event);
            return Main.getCommandListener().getCommandHandler().processCommand(result, event);
        }
        return "false";
    }

    private String getReplacement(String s, Event event, String[] params) {

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
        } else if (s.equals("$#")) {
            return String.valueOf(params.length - 1);
        } else if (s.matches("\\$IF\\((.*?),(.*?),(.*)\\)")) {
            s = getOuterParentheses(s);

            //Any nested parenthesis
            s = parseNested(s, event, params);

            Matcher subMatcher = pattern.matcher(s);
            StringBuffer subBuffer = new StringBuffer();

            //Match other replacements
            while (subMatcher.find()) {
                subMatcher.appendReplacement(subBuffer, getReplacement(subMatcher.group(), event, params));
            }
            subMatcher.appendTail(subBuffer);
            s = subBuffer.toString();

            String[] assessment = s.split("(?<!\\\\),");

            //Return first parameter on successful assess, else return second
            if (assess(assessment[0].split("(?<!\\\\)(?=(==|!=|c=|<=|>=|<|>))|(?<=(==|!=|c=|<=|>=|<|>))(?<!\\\\(==|!=|c=|<=|>=|<|>))"))) return assessment[1];
            else return (assessment.length > 2 ? Matcher.quoteReplacement(assessment[2]) : "");

        } else if (s.matches("\\$EXEC\\((.*?)\\)")) {
            s = getOuterParentheses(s);

            //Parse nested parenthesis
            s = parseNested(s, event, params);

            Matcher submatcher = pattern.matcher(s);
            StringBuffer buffer = new StringBuffer();

            //Find and replace submatches
            while (submatcher.find()) {
                submatcher.appendReplacement(buffer, Matcher.quoteReplacement(getReplacement(submatcher.group(), event, params)));
            }

            submatcher.appendTail(buffer);

            String command = buffer.toString();
            if (Main.getCommandListener().getCommandHandler().isCommand(command.substring(0, command.indexOf(" ")))) {
                String r = "";

                Settings.suppressOutput = true;
                if (Settings.aliases.containsKey(command.substring(0, command.indexOf(" ")))) r = runAlias(command.split("\\s+"), event);
                else r = Main.getCommandListener().getCommandHandler().processCommand(command, event);
                Settings.suppressOutput = false;

                return r;
            } else return "";
        }

        return "";
    }

    private boolean assess(String[] check) {
        //Remove trailing spaces
        String input = check[0].trim();

        if (input.equals("true")) return true;
        if (input.equals(" ") && check.length == 2) return check[2].equals("==");
        if (input.equals("false")) return false;
        if (check.length < 3) return false;
        String compare = check[2].trim();
        //TODO: Solve math?

        try {
            if (check[1].equals("==")) return input.equals(compare);
            if (check[1].equals("!=")) return !input.equals(compare);
            if (check[1].equals("c=")) return input.contains(compare);

            //Do digits
            if (input.matches("\\d+") && compare.matches("\\d+")) {
                double numberA = Double.parseDouble(input);
                double numberB = Double.parseDouble(compare);

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

    private String getOuterParentheses(String pattern) {
        int start = pattern.indexOf("(");
        int end = 0;
        String r = "";

        //Get the first unescaped parenthesis
        if (start != 0) {
            while (pattern.charAt(start -  1) == '\\') {
                start = pattern.indexOf("(", start);
            }
        }

        r = pattern.substring(start + 1);
        int parenthesisLevel = 1;

        for (int i = 1; i < r.length(); i++) {
            if (r.charAt(i - 1) == '\\') continue;
            switch (r.charAt(i)) {
                case '(':
                    parenthesisLevel++;
                    end = i;
                    break;
                case ')':
                    parenthesisLevel--;
                    end = i;
                    break;
                default:
                    continue;
            }

            if (parenthesisLevel == 0) { break; }
            else if (parenthesisLevel < 0) return ""; //Should really not happen, but whatever
        }

        return r.substring(0, end);
    }

    private String parseNested(String s, Event event, String[] params) {
        //Parse nested parenthesis
        Matcher specialMatcher = specialPattern.matcher(s);
        while (specialMatcher.find()) {
            //The start of the match in the string
            int start = specialMatcher.start();
            //The length of the start, ex $IF(.....) is 5
            int off = specialMatcher.group().replaceAll("\\((.*)\\)", "").length() + 2;

            //Stop at startposition plus the full length of the parenthesis
            int end = start + getOuterParentheses(s.substring(start)).length() + off;

            //Get replacement for the parenthesis
            String replace = getReplacement(s.substring(start, end), event, params);

            //Replace the string in s
            s = s.substring(0, start) + replace + s.substring(end);

            //Reset the matcher in case of more matches
            specialMatcher.reset(s);
        }

        return s;
    }
}

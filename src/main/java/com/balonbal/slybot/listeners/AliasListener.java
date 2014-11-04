package com.balonbal.slybot.listeners;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.lib.Reference;
import com.balonbal.slybot.lib.Settings;
import org.apache.commons.lang3.StringUtils;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AliasListener extends ListenerAdapter<SlyBot> {

    private static final Pattern pattern = Pattern.compile("(?<!\\\\)\\$(USER|CHANNEL|@|\\d)");

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

            while(matcher.find()) {
                matcher.appendReplacement(buffer, getReplacement(matcher, event, params));
            }

            matcher.appendTail(buffer);

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
        }

        return "";
    }
}

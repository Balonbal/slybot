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

public class AliasListener extends ListenerAdapter<SlyBot> {

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
        User u = null;
        Channel c = null;
        if (event instanceof MessageEvent) {
            u = ((MessageEvent) event).getUser();
            c = ((MessageEvent) event).getChannel();
        } else if (event instanceof PrivateMessageEvent) {
            u = ((PrivateMessageEvent) event).getUser();
        }

        String alias = params[0].toUpperCase();
        if (Settings.aliases.containsKey(alias)) {
            String command = Settings.aliases.get(alias);
            System.out.println(command);
            if (command.contains("$USER") && u != null) {
                command = command.replaceAll("\\$USER", u.getNick());
            }
            if (command.contains("$CHANNEL") && c != null) {
                command = command.replaceAll("\\$CHANNEL", c.getName());
            }
            if (command.contains("$@")) {
                String[] newParams = new String[params.length - 1];
                System.arraycopy(params, 1, newParams, 0, newParams.length);
                command = command.replace("$@", StringUtils.join(newParams, " "));
            }
            int i = 1;
            while (command.contains("$" + i)) {
                command = command.replaceAll("\\$" + i, params[i-1]);
                i++;
            }

            Main.getCommandListener().getCommandHandler().processCommand(command, event);
        }
    }
}

package com.balonbal.slybot.listeners;

import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.core.CommandHandler;
import com.balonbal.slybot.lib.Reference;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

public class CommandListener extends ListenerAdapter<SlyBot> {

    private final CommandHandler commandHandler;

    public CommandListener(CommandHandler handler) {
        commandHandler = handler;
        commandHandler.addCommands();
    }

    @Override
    public void onMessage(MessageEvent<SlyBot> e) {
        for (String trigger: Reference.PREFIXES) {
            if (e.getMessage().toLowerCase().startsWith(trigger.toLowerCase())) {
                String command = e.getMessage().substring(trigger.length());
                commandHandler.processCommand(command, e);
            }
        }
    }

    @Override
    public void onPrivateMessage(PrivateMessageEvent<SlyBot> e) {
        commandHandler.processCommand(e.getMessage(), e);
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

}

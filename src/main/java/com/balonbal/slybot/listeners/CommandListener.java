package com.balonbal.slybot.listeners;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.core.CommandHandler;
import com.balonbal.slybot.lib.Reference;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandListener extends ListenerAdapter<SlyBot> {

    private final CommandHandler commandHandler;
    private static Pattern trigger;

    public CommandListener(CommandHandler handler) {
        commandHandler = handler;
        commandHandler.addCommands();
    }

    @Override
    public void onMessage(MessageEvent<SlyBot> e) {
        Matcher matcher = trigger.matcher(e.getMessage());
        StringBuffer buffer = new StringBuffer();

        if (matcher.find()) {
            if (matcher.start() == 0) {
                matcher.appendReplacement(buffer, "");
                String command = matcher.appendTail(buffer).toString();
                System.out.println(command);
                commandHandler.processCommand(command, e);
            }
        }
    }

    @Override
    public void onPrivateMessage(PrivateMessageEvent<SlyBot> e) {
        commandHandler.processCommand(e.getMessage(), e);
    }

    @Override
    public void onConnect(ConnectEvent<SlyBot> event) throws Exception {
        //Start tasks that needs the bot to be connected
        event.getBot().onConnected();
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public void setTrigger() {
        String regex = Reference.PREFIX_REGEX.replaceAll("\\$BOTNICK", Main.getBot().getNick());
        trigger = Pattern.compile(regex);
    }

}

package com.balonbal.slybot.listeners;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.config.ChannelConfig;
import com.balonbal.slybot.core.CommandHandler;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandListener extends ListenerAdapter<SlyBot> {

    private final CommandHandler commandHandler;

    public CommandListener(CommandHandler handler) {
        commandHandler = handler;
        commandHandler.addCommands();
    }

    @Override
    public void onMessage(MessageEvent<SlyBot> e) {

        //Use channel defined triggers
        String command = getCommand(e);
        commandHandler.processCommand(command, e);
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

    public String getCommand(MessageEvent e) {
        ChannelConfig config = (ChannelConfig) Main.getConfig().getConfig("config" + e.getChannel().getName());

        //Get the regex for the channel
        Pattern trigger = config.getTrigger();

        Matcher matcher = trigger.matcher(e.getMessage());
        StringBuffer buffer = new StringBuffer();

        if (matcher.find()) {
            if (matcher.start() == 0) {
                //Remove the trigger from the string and return
                matcher.appendReplacement(buffer, "");
                return matcher.appendTail(buffer).toString();
            }
        }

        return "";
    }

}

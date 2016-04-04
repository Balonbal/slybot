package com.balonbal.slybot.commands;

import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.lib.Reference;
import com.balonbal.slybot.lib.Settings;
import com.balonbal.slybot.util.LoggerUtil;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import java.io.File;
import java.io.IOException;
import java.sql.Ref;

public class CommandStats implements Command {
    @Override
    public String getTrigger() {
        return "stats";
    }

    @Override
    public int requiresOP() {
        return Reference.REQUIRES_OP_ANY;
    }

    @Override
    public boolean channelCommand() {
        return true;
    }

    @Override
    public boolean pmCommand() {
        return true;
    }

    @Override
    public String[] help() {
        return new String[0];
    }

    @Override
    public String run(String[] parameters, Event<SlyBot> event) {
        StringBuilder builder = new StringBuilder();
        boolean channelCommand = !(event instanceof PrivateMessageEvent);

        builder.append("--- Current BOT Statistics ---\n");

        //Channel stats
        if (channelCommand) {
            MessageEvent e = (MessageEvent) event;

            //Get channel log
            String path = Reference.CONFIG_CHANNEL_FILE;
            path = path.replaceAll("\\$NETWORK", Settings.network);
            path = path.replaceAll("\\$CHANNEL", e.getChannel().getName());


            builder.append("Recorded messages from this channel: ");
            builder.append(Colors.BLUE);
            try {
                builder.append(LoggerUtil.countLines(path));
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            builder.append("\n");

        }

        event.getBot().replyLots(event, builder.toString());
        return null;
    }
}

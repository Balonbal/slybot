package com.balonbal.slybot.commands;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.lib.Reference;
import com.balonbal.slybot.lib.Settings;
import com.balonbal.slybot.util.LoggerUtil;
import com.balonbal.slybot.util.stats.ChannelStats;
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

            ChannelStats stats = Main.getStatsCacher().getStats(e.getChannel().getName());

            //Messages
            builder.append("Recorded messages from this channel: ");
            builder.append(Colors.BLUE);
            builder.append(stats.getMessages());
            builder.append("\n");

            //Active user
            builder.append("Most active user: ");
            builder.append(Colors.BLUE);
            builder.append(stats.getMostActiveUser());
            builder.append(Colors.NORMAL);
            builder.append(" (");
            builder.append(Colors.GREEN + Colors.BOLD);
            builder.append(stats.getMessageCount().get(stats.getMostActiveUser()));
            builder.append(Colors.NORMAL);
            builder.append(" messages | ");
            builder.append(Colors.PURPLE);
            builder.append(Math.round(stats.getMessageCount().get(stats.getMostActiveUser()) * 100 / stats.getMessages()));
            builder.append(Colors.NORMAL);
            builder.append("%)\n");

            //Commands
            builder.append("Most used commands: ");
            for (String c: stats.getMostCommands(5)) {
                builder.append(Colors.BLUE);
                builder.append(c);
                builder.append(Colors.NORMAL);
                builder.append(" (");
                builder.append(Colors.GREEN);
                builder.append(stats.getCommandCount().get(c));
                builder.append(Colors.NORMAL);
                builder.append(") ");
            }
            builder.append("\n");
        }

        event.getBot().replyLots(event, builder.toString());
        return null;
    }
}

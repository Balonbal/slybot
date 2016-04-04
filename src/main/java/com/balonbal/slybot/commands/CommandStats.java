package com.balonbal.slybot.commands;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.core.StatsCacher;
import com.balonbal.slybot.lib.Reference;
import com.balonbal.slybot.lib.Settings;
import com.balonbal.slybot.util.LoggerUtil;
import com.balonbal.slybot.util.TimeDateUtil;
import com.balonbal.slybot.util.stats.ChannelStats;
import org.pircbotx.Channel;
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

        //Input
        int level = 1; //0 for minimal, 1 for normal, 2 for more
        if (parameters.length > 1) {
            if (parameters[1].equalsIgnoreCase("--more") || parameters[1].equalsIgnoreCase("-m")) level = 2; //More output
            else if (parameters[1].equalsIgnoreCase("--less") || parameters[1].equalsIgnoreCase("-l")) level = 0;

        }

        if (level > 1) builder.append("--- Current BOT Statistics ---\n");

        StatsCacher cacher = Main.getStatsCacher();
        long onlineTime = cacher.currOnline + (System.currentTimeMillis() - cacher.lastOnlineTick);
        long uptime = cacher.onlineTime + (System.currentTimeMillis() - cacher.lastOnlineTick);
        String timePattern = Colors.BLUE + "$dd, $h:$m:$s" + Colors.NORMAL;

        //Uptime
        builder.append("Current uptime: ");
        builder.append(TimeDateUtil.stringify(onlineTime, timePattern));
        builder.append("\n");

        if (level > 0) {
            //Historical uptime
            builder.append("All-time uptime: ");
            builder.append(TimeDateUtil.stringify(uptime, timePattern));
            builder.append(" (");
            builder.append(Colors.PURPLE);
            builder.append(uptime * 100 / (cacher.offlineTime + cacher.onlineTime));
            builder.append(Colors.NORMAL);
            builder.append("%)\n");
        }

        //Channels
        if (level > 1) {
            builder.append("Currently connected channels: ");
            for (Channel c: event.getBot().getUserBot().getChannels()) {
                if (c.isOp(event.getBot().getUserBot())) builder.append(Colors.PURPLE);
                else if (c.hasVoice(event.getBot().getUserBot())) builder.append(Colors.GREEN);
                else builder.append(Colors.BLUE);
                builder.append(c.getName());
                builder.append(Colors.NORMAL);
                builder.append(", ");
            }
            builder.append("\n");
        }

        //Channel stats
        if (channelCommand) {
            MessageEvent e = (MessageEvent) event;

            ChannelStats stats = Main.getStatsCacher().getStats(e.getChannel().getName());

            //Messages
            builder.append("Recorded messages from this channel: ");
            builder.append(Colors.BLUE);
            builder.append(stats.getMessages());
            builder.append("\n");

            if (level > 0) {
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
            }

            if (level > 1) {
                //Commands
                builder.append("Most used commands: ");
                for (String c : stats.getMostCommands(5)) {
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

            //TODO Add game stats
        }

        event.getBot().replyLots(event, builder.toString());
        return null;
    }
}

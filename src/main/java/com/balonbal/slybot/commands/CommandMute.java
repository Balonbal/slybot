package com.balonbal.slybot.commands;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.lib.Settings;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;

public class CommandMute implements Command {
    @Override
    public String[] getTriggers() {
        return new String[] {
                "mute",
                "silence"
        };
    }

    @Override
    public boolean requiresOP() {
        return true;
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
        return new String[] {
               "Mute the bot in a specified channel. By default, the current channel is used",
                "SYNTAX: " + Colors.BOLD + "MUTE [-u|--unmute] [#channel]",
                "Use -u or --unmute to remove mute"
        };
    }

    @Override
    public void run(User user, Channel channel, String[] params) {
        String c = channel.getName();
        if (params[0].equalsIgnoreCase("-u") || params[0].equalsIgnoreCase("--unmute")) {
            if (params.length > 1) {
                c = params[1];
            }

            String newMuted = "";
            for (String s: Settings.mutedChannels) {
                if (s.equalsIgnoreCase(c)) continue;
                newMuted += (newMuted.equals("") ? "" : ",") + s;
            }

            Main.getConfig().changeSetting("mutedChannels", newMuted);
            Main.getBot().reply(channel, user, "No longer muted in channel " + c);
        } else {
            if (params.length > 0) {
                c = params[0];
            }

            System.out.println("Muted channel " + c);
            Main.getConfig().appendSetting("mutedChannels", ",", c);
        }
    }
}

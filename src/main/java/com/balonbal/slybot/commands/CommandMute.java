package com.balonbal.slybot.commands;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.lib.Reference;
import com.balonbal.slybot.lib.Settings;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.MessageEvent;

public class CommandMute implements Command {
    @Override
    public String getTrigger() {
        return "mute";
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
        return new String[] {
               "Mute the bot in a specified channel. By default, the current channel is used",
                "SYNTAX: " + Colors.BOLD + "MUTE [-u|--unmute] [#channel]",
                "Use -u or --unmute to remove mute"
        };
    }

    @Override
    public void run(String[] params, Event<SlyBot> event) {
        String channel = "";

        if (event instanceof MessageEvent) {
            channel = ((MessageEvent) event).getChannel().getName();
        }

        if (params.length > 1) {
            if (params[1].equalsIgnoreCase("-u") || params[1].equalsIgnoreCase("--unmute")) {
                if (params.length > 2) {
                    channel = params[2];
                }

                String newMuted = "";
                for (String s : Settings.mutedChannels) {
                    if (s.equalsIgnoreCase(channel)) continue;
                    newMuted += (newMuted.equals("") ? "" : ",") + s;
                }

                Main.getConfig().changeSetting("mutedChannels", newMuted);
                event.getBot().reply(event, "No longer muted in channel " + channel);
            } else {
                channel = params[1];
            }

            return;
        }

        System.out.println("Muted channel " + channel);
        Main.getConfig().appendSetting("mutedChannels", ",", channel);
    }
}

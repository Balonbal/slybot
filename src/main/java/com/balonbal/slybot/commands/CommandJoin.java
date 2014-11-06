package com.balonbal.slybot.commands;

import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.lib.Reference;
import org.pircbotx.Channel;
import org.pircbotx.User;

import com.balonbal.slybot.Main;
import org.pircbotx.hooks.Event;

public class CommandJoin implements Command {

	@Override
	public void run(String[] params, Event<SlyBot> event) {
		if (params.length > 1 && params[1] != null) {
            String channel = params[1];
			if (params[1].equalsIgnoreCase("-a") || params[1].equalsIgnoreCase("--auto")) {
                channel = params[2];
                event.getBot().getConfig().appendSetting(Reference.CONFIG_CHANNELS, channel);
			}
			System.out.println("Joining channel: " + channel);
			event.getBot().sendIRC().joinChannel(channel);
		} else {
			event.getBot().reply(event, "Too few parameters!");
		}
	}

	@Override
	public String[] help() {
		return new String[] {
				"Used to make the bot join a new channel, only bot operators can do this",
				"SYNTAX: JOIN #<channel>",
				"Example: join #example"
		};
	}

	@Override
	public String getTrigger() {
		return "join";
	}

	@Override
	public int requiresOP() {
		return Reference.REQUIRES_OP_BOT;
	}

	@Override
	public boolean channelCommand() {
		return false;
	}

	@Override
	public boolean pmCommand() {
		return true;
	}

}

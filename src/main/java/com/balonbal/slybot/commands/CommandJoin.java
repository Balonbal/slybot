package com.balonbal.slybot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.balonbal.slybot.Main;

public class CommandJoin implements Command {

	@Override
	public void run(User user, Channel channel, String[] params) {
		if (params.length > 0 && params[0] != null) { 
			if (params[0].equalsIgnoreCase("-a") || params[0].equalsIgnoreCase("-a")) {
				Main.getConfig().appendSetting("default_channels", ",", params[1]);
			}
			System.out.println("Joining channel: " + params[0]);
			Main.getBot().sendIRC().joinChannel(params[0]);
		} else {
			user.send().message("Too few parameters!");
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
	public String[] getTriggers() {
		return new String[] {
				"join"
		};
	}

	@Override
	public boolean requiresOP() {
		return true;
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

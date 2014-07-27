package com.balonbal.slybot.commands;

import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.core.CommandHandler;

public class CommandLeave implements Command {

	@Override
	public void run(User user, Channel channel, String[] params) {

		if (params.length > 0) {
			if (params[0].equals("-s")) {
				//Check for botOP NOTE: this is not channel-op
				if (CommandHandler.isBotOP(user)) {
					channel.send().message("SlyBot is shutting down.");
					//Shutdown the bot
					Main.getBot().shutdown();
				}
			}
		}
		
		System.out.println("leaving channel " + channel.getName());
		//Else leave the channel
		channel.send().part("told by operator to leave");
	}

	@Override
	public String[] help() {
		return new String[] {
				"Used to make the bot leave the channel, can only be issued by OPs",
				"SYNTAX: " + Colors.BOLD + "LEAVE [-s]",
				"-s        Shut down the bot"
		};
	}

	@Override
	public String[] getTriggers() {
		return new String[] {
				"leave",
				"part"
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
		return false;
	}

}

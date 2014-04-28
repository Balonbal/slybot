package slybot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;

import slybot.SlyBot;

public abstract class Command {
	
	public boolean requiresOP;
	public boolean PMCommand;
	public boolean isChannelCommand;
	public String command;
	
	public Command(String command) {
		this(command, false, false, true);
	}
	
	public Command(String command, boolean needsop, boolean pmcommand, boolean isChannelCommand) {
		this.command = command;
		this.requiresOP = needsop;
		this.PMCommand = pmcommand;
		this.isChannelCommand = isChannelCommand;
	}
	
	public void sendHelp(User u, Channel c, String[] params) {
		for (String s: help()) {
			if (c == null) {
				u.send().message(s);
			} else {
				c.send().message(s);
			}
		}
	}
	
	public abstract String[] help();
	
	public abstract void run(SlyBot bot, User user, Channel channel, String[] params);
}

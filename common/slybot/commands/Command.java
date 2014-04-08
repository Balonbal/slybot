package slybot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;

import slybot.SlyBot;

public abstract class Command {
	
	boolean requiresOP;
	boolean PMCommand;
	String command;
	
	public Command(String command) {
		this(command, false, false);
	}
	
	public Command(String command, boolean needsop, boolean pmcommand) {
		this.command = command;
		this.requiresOP = needsop;
	}
	
	public abstract String[] help();
	
	public abstract void run(SlyBot bot, User user, Channel channel, String[] params);

}

package slybot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;

import slybot.SlyBot;

public abstract class Command {
	
	boolean requiresOP;
	boolean PMCommand;
	boolean isChallenge;
	public String command;
	
	public Command(String command) {
		this(command, false, false, false);
	}
	
	public Command(String command, boolean needsop, boolean pmcommand, boolean isChallenge) {
		this.command = command;
		this.requiresOP = needsop;
		this.isChallenge = isChallenge;
	}
	
	public abstract String[] help();
	
	public abstract void run(SlyBot bot, User user, Channel channel, String[] params);
	
	public abstract User challenge(SlyBot bot, User usera, User userb, Channel channel, String[] params);

}

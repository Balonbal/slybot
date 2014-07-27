package com.balonbal.slybot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;

public interface Command {
	
	/**
	 * Specify the strings that will cause the commandhandler to call run
	 * @return an array of triggers
	 */
	public abstract String[] getTriggers();
	
	/**
	 * Whether or not the command requires operator status to run.
	 * Note that both botops and channel operators will be considered valid
	 */
	public abstract boolean requiresOP();
	
	/**
	 * Define if the command can be used in channels 
	 * @return true if it can, false if it cannot
	 */
	public abstract boolean channelCommand();
	
	/**
	 * Specify if the command may be used over private messaging
	 * @return true if it can, false if it cannot
	 */
	public abstract boolean pmCommand();
	
	/**
	 * The message/s a user will see if they run help
	 * @return an array where each element is a line in the returned message
	 */
	public abstract String[] help();
	
	/**
	 * Anything within this method will be run when a trigger from the command is found and {@link #requiresOP()}, {@link #channelCommand()} and {@link #pmCommand()} is satisfied
	 * @param user the user performing the command
	 * @param channel the channel the command was used in, will be null for private messages
	 * @param params all, if any, parameters included by the user
	 */
	public abstract void run(User user, Channel channel, String[] params);
}

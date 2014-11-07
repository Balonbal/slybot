package com.balonbal.slybot.commands;

import com.balonbal.slybot.SlyBot;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.Event;

public interface Command {
	
	/**
	 * Specify the strings that will cause the commandhandler to call run
	 * @return an array of triggers
	 */
	public abstract String getTrigger();
	
	/**
	 * Whether or not the command requires operator status to run.
     * @return the integer value of the permission needed. See {@link com.balonbal.slybot.lib.Reference} for the exact values.
	 */
	public abstract int requiresOP();

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
	 * @param parameters the parameters sent from the user. An array with the first element as the name of the command.
     * @param event the event that occurred. This contains useful information like channels, users, timestamps etc.
     * @return the string value to be used for piping commands, for example for the $EXEC in aliases
	 */
	public abstract String run(String[] parameters, Event<SlyBot> event);
}

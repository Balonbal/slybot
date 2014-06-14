package slybot.core;

import org.pircbotx.Channel;
import org.pircbotx.User;

import slybot.Main;
import slybot.commands.Command;
import slybot.lib.Settings;

public class CommandHandler {
	
	public static void processCommand(User user, Channel channel, String message, boolean isOP) {
		String cmd;
		String[] params;
		
		//If the message has parameters
		if (message.contains(" ")) {
			cmd = message.substring(0, message.indexOf(" "));
			message = message.substring(message.indexOf(" ")+1);
			//This will split at blank spaces, tabs etc
			params = message.split("\\s+");
		} else {
			cmd = message;
			params = new String[] { };
		}
		
		System.out.println("Trying command \"" + cmd + "\" with " + params.length + " parameters from user \"" + user.getNick() + "\", " + (isOP? "Operator":""));
		
		Command c = getCommand(cmd);
		if (c != null) {
			//If the command requires op, check for OP
			if (!(c.requiresOP() && !isOP) || isBotOP(user)) {
				//Only do commands in appropriate channels.
				if ((c.channelCommand() && channel != null) || (c.pmCommand() && channel == null) || c.channelCommand() && c.pmCommand()) {
					c.run(user, channel, params);	
				}
			}
		}

	}
	
	public static boolean isBotOP(User user) {
		//Only include users verified by the nickserv
		if (user.isVerified()) {
			for (String s: Settings.botops) {
				if (s.equalsIgnoreCase(user.getNick())) {
					return true;
				}
			}
			
		}
		return false;
	}
	
	public static Command getCommand(String cmd) {
		for (Command c: Main.getListener().getCommands()) {
			for (String trigger: c.getTriggers()) {
				if (cmd.equalsIgnoreCase(trigger)) {
					return c;
				}
			}
		}
		
		return null;
	}

}

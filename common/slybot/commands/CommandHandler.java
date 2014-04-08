package slybot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;

import slybot.SlyBot;
import slybot.lib.Settings;

public class CommandHandler {
	
	//Add the available commands
	private static Command[] commands = {
			new CommandLeave(),
			new CommandJoin(),
			new CommandClaim(),
			new CommandRTD()
	};
	
	public static void processCommand(SlyBot bot, User user, Channel channel, String message, boolean isOP) {
		String cmd;
		String[] params;
		
		System.out.println("Checking string \"" + message + "\" for commands");
		
		if (message.contains(" ")) {
			cmd = message.substring(0, message.indexOf(" "));
			message = message.substring(message.indexOf(" ")+1);
			System.out.println(message);
			params = message.split("\\s+");
		} else {
			cmd = message;
			params = new String[] { };
		}
		
		System.out.println("Trying command \"" + cmd + "\" with " + params.length + " parameters from user \"" + user.getNick() + "\", " + (isOP? "Operator":""));
		
		for (Command command: commands) {
			if (cmd.equalsIgnoreCase(command.command)) {
				System.out.println("Command found, issuing");
				//If the command requires op, check for OP
				if (!(command.requiresOP && !isOP) || isBotOP(user)) {
					command.run(bot, user, channel, params);
				}
			}
		}

	}
	
	private static boolean isBotOP(User user) {
		if (user.isVerified()) {
			for (String s: Settings.botops) {
				if (s.equalsIgnoreCase(user.getNick())) {
					return true;
				}
			}
			
		}
		return false;
	}

}

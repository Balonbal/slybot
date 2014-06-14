package slybot.commands;

import java.util.ArrayList;

import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;

import slybot.Main;
import slybot.core.CommandHandler;

public class CommandHelp implements Command {

	@Override
	public String[] help() {
		return new String[] {
				"Used to clearify the use of a command.",
				"SYNTAX: " + Colors.BOLD + "HELP [command]" + Colors.NORMAL,
				"Example: HELP RTD",
				"Running the command with no parameters will return a list of commands"
		};
	}

	@Override
	public void run(User user, Channel channel, String[] params) {
		String[] response = null;
		if (params.length == 0) {
			//Fetch all the commands
			ArrayList<Command> cmds = Main.getListener().getCommands();
			
			response = new String[cmds.size() + 2];
			response[0] = "Available commands: ";
			
			for (int i = 0; i < cmds.size(); i++) {
				//Add the first trigger for each command
				response[i+1] = cmds.get(i).getTriggers()[0]; 
			}
			
			response[response.length-1] = "Total: " + Colors.BOLD + cmds.size() + Colors.NORMAL + " items.";
		} else {
			if (CommandHandler.getCommand(params[0]) != null) {
				//Fetch the help strings for the given command
				response = CommandHandler.getCommand(params[0]).help();
			}
		}
		
		for (String s: response) {
			//if  sent on PM
			if (channel == null) {
				user.send().message(s);
			} else {
				channel.send().message(s);
			}
		}
	}

	@Override
	public String[] getTriggers() {
		return new String[] {
				"help",
				"halp",
				"command",
				"commands"
		};
	}

	@Override
	public boolean requiresOP() {
		return false;
	}

	@Override
	public boolean channelCommand() {
		return true;
	}

	@Override
	public boolean pmCommand() {
		return true;
	}

}

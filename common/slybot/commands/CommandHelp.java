package slybot.commands;

import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;

import slybot.SlyBot;
import slybot.core.CommandHandler;

public class CommandHelp extends Command {

	public CommandHelp() {
		super("help", false, true, true);
	}

	@Override
	public String[] help() {
		return new String[] {
				"Used to clearify the use of a command.",
				"SYNTAX: " + Colors.BOLD + "HELP <command>" + Colors.NORMAL,
				"Example: HELP RTD"
		};
	}

	@Override
	public void run(SlyBot bot, User user, Channel channel, String[] params) {
		String[] response = null;
	    if (params.length > 0) {
			if (CommandHandler.getCommand(params[0]) != null) {
				//TODO May cause nullpointers
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

}

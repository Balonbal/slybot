package slybot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;

import slybot.SlyBot;
import slybot.core.CommandHandler;

public class CommandHelp extends Command {

	public CommandHelp() {
		super("help", false, true, true);
	}

	@Override
	public String[] help() {
		return null;
	}

	@Override
	public void run(SlyBot bot, User user, Channel channel, String[] params) {
		if (params.length > 0) {
			if (CommandHandler.getCommand(params[0]) != null) {
				
			}
		}
		//if  sent on PM
		if (channel == null) {
			
		}
	}

}

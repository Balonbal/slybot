package slybot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;

import slybot.SlyBot;
import slybot.core.SlyConfiguration;
import slybot.lib.Settings;

public class CommandSetPass extends Command {

	public CommandSetPass() {
		super("setPass", true, true, false);
	}

	@Override
	public String[] help() {
		return new String[] {
				"Set the password for operator commands.",
				"Will require the current password if one is set.",
				"SYNTAX: setpass [current password] [new password]"
		};
	}

	@Override
	public void run(SlyBot bot, User user, Channel channel, String[] params) {
		if (params.length == 1) {
			//Check if no password is set
			if (Settings.operatorpass.equals("")) {
				SlyConfiguration.changeSetting("operatorpass", params[0]);
				user.send().message("Sucessfully updated password.");
			}
		} else if (params.length == 2){
			if (Settings.operatorpass.equals(params[0])) {
				SlyConfiguration.changeSetting("operatorpass", params[1]);
				user.send().message("Sucessfully updated password.");
			}
		}
	}
}

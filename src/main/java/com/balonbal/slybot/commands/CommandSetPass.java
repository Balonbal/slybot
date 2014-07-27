package com.balonbal.slybot.commands;

import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.lib.Settings;

public class CommandSetPass implements Command {

	@Override
	public String[] help() {
		return new String[] {
				"Set the password for operator commands.",
				"Will require the current password if one is set.",
				"SYNTAX: " + Colors.BOLD + "setpass [current password] <new password>" + Colors.NORMAL
		};
	}

	@Override
	public void run(User user, Channel channel, String[] params) {
		if (params.length == 1) {
			//Check if no password is set
			if (Settings.operatorpass.equals("")) {
				Main.getConfig().changeSetting("operatorpass", params[0]);
				user.send().message("Sucessfully updated password.");
			}
		} else if (params.length == 2){
			if (Settings.operatorpass.equals(params[0])) {
				Main.getConfig().changeSetting("operatorpass", params[1]);
				user.send().message("Sucessfully updated password.");
			}
		}
	}

	@Override
	public String[] getTriggers() {
		return new String[] {
				"setpass",
				"pass"
		};
	}

	@Override
	public boolean requiresOP() {
		return true;
	}

	@Override
	public boolean channelCommand() {
		return false;
	}

	@Override
	public boolean pmCommand() {
		return true;
	}
}

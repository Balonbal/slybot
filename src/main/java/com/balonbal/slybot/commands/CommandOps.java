package com.balonbal.slybot.commands;

import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.core.SlyConfiguration;
import com.balonbal.slybot.lib.Settings;

public class CommandOps implements Command {

	@Override
	public String[] help() {
		return new String[] {
				"Used to list, add and/or remove operators for this bot. Does " + Colors.BOLD + "NOT" + Colors.NORMAL + " affect any channels",
				"Can only be issued by bot-operators per PM",
				"Will return a list of the current operators if no parameters are passed",
				"SYNTAX: " + Colors.BOLD + "OPS [ <ADD|REMOVE> <user> <botpassword> ]" + Colors.NORMAL,
				"Example: " + Colors.BOLD + "OPS ADD sly mySecretPassword123" + Colors.NORMAL
		};
	}

	@Override
	public void run(User user, Channel channel, String[] params) {
		//Double check that this is in fact requested through a pm
		if (channel == null) {
			if (params.length > 2) {
				String nick = params[1];
				String pwd = params[2];
				
				if (pwd.equals(Settings.operatorpass)) {
					if (params[0].equalsIgnoreCase("add")) {
                        Main.getConfig().appendSetting("botops", ",", nick);
                    } else if (params[0].equalsIgnoreCase("remove")) {
						//Owner cannot be removed from the ops
						if (!nick.equalsIgnoreCase(Settings.owner)) {
							Main.getConfig().changeSetting("botops", SlyConfiguration.getSetting("botops").replace("," + nick, ""));
						}
					}
				}
			}
			user.send().message("The current bot operators are: ");
			for (String s: Settings.botops) {
				user.send().message(" - " + s);
			}
			user.send().message("Total: " + Settings.botops.length + " items");
		}

	}

	@Override
	public String[] getTriggers() {
		return new String[] {
				"ops",
				"operators"
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

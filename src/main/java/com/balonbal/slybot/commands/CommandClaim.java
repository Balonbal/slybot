package com.balonbal.slybot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.lib.Settings;

public class CommandClaim implements Command {

	@Override
	public String[] help() {
		return new String[] {
				"Used to initialy gain control over the bot. You only need to do this once."
		};
	}

	@Override
	public void run(User user, Channel channel, String[] params) {
		
		//if the command is sent in a channel, disregard it
		if (channel != null) {
			return;
		}
		
		if (Settings.owner.equals("")) {
			//check if the user is verified with nickserv
			if (user.isVerified()) {
				//update config accordingly
				Main.getConfig().changeSetting("owner", user.getNick());
				Main.getConfig().changeSetting("botops", user.getNick());
				user.send().message("Successfully claimed bot.");
			} else {
				user.send().message("Please register your nick before claiming this bot");
			}
			
		} else if (Settings.owner.equalsIgnoreCase(user.getNick())){
			user.send().message("You already own this bot, silly");
		} else {
			user.send().message("Error! Bot already claimed by " + Settings.owner + ".");
		}
	}

	@Override
	public String[] getTriggers() {
		return new String[] {
				"claim"
		};
	}

	@Override
	public boolean requiresOP() {
		//No one is botop before the initial claim
		return false;
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

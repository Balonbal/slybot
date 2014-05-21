package slybot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;

import slybot.Main;
import slybot.SlyBot;
import slybot.lib.Settings;

public class CommandClaim extends Command {

	public CommandClaim() {
		super("claim", false, true, false);
	}

	@Override
	public String[] help() {
		return new String[] {
				"Used to initialy gain control over the bot. You only need to do this once."
		};
	}

	@Override
	public void run(SlyBot bot, User user, Channel channel, String[] params) {
		
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

}

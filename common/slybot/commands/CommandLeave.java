package slybot.commands;

import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;

import slybot.Main;
import slybot.SlyBot;
import slybot.core.CommandHandler;

public class CommandLeave extends Command {

	public CommandLeave() {
		super("leave", true, false, true);
	}

	@Override
	public void run(SlyBot bot, User user, Channel channel, String[] params) {

		if (params.length > 0) {
			if (params[0].equals("-s")) {
				//Check for botOP NOTE: this is not channel-op
				if (CommandHandler.isBotOP(user)) {
					channel.send().message("SlyBot is shutting down.");
					//Shutdown the bot
					Main.getBot().shutdown();
				}
			}
		}
		
		System.out.println("leaving channel " + channel.getName());
		//Else leave the channel
		channel.send().part("told by operator to leave");
	}

	@Override
	public String[] help() {
		return new String[] {
				"Used to make the bot leave the channel, can only be issued by OPs",
				"SYNTAX: " + Colors.BOLD + "LEAVE [-s]",
				"-s        Shut down the bot"
		};
	}

}

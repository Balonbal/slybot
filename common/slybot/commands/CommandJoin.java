package slybot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;

import slybot.Main;
import slybot.SlyBot;

public class CommandJoin extends Command {

	public CommandJoin() {
		super("join", true, true, false);
	}

	@Override
	public void run(SlyBot bot, User user, Channel channel, String[] params) {
		if (params.length > 0 && params[0] != null) { 
			if (params[0].equalsIgnoreCase("-a") || params[0].equalsIgnoreCase("-a")) {
				Main.getConfig().appendSetting("default_channels", ",", params[1]);
			}
			System.out.println("Joining channel: " + params[0]);
			bot.sendIRC().joinChannel(params[0]);
		} else {
			user.send().message("Too few parameters!");
		}
	}

	@Override
	public String[] help() {
		return new String[] {
				"Used to make the bot join a new channel, only bot operators can do this",
				"SYNTAX: JOIN <channel>",
				"Example: join #test"
		};
	}

}

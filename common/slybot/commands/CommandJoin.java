package slybot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;

import slybot.SlyBot;

public class CommandJoin extends Command {

	public CommandJoin() {
		super("join", false, true, false);
	}

	@Override
	public void run(SlyBot bot, User user, Channel channel, String[] params) {
		if (params.length > 0 && params[0] != null) { 
			System.out.println(params[0]);
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

	@Override
	public User challenge(SlyBot bot, User usera, User userb, Channel channel, String[] params) {
		return null;
	}

}

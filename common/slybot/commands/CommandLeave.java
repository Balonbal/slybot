package slybot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;

import slybot.SlyBot;

public class CommandLeave extends Command {

	public CommandLeave() {
		super("leave", true, false);
	}

	@Override
	public void run(SlyBot bot, User user, Channel channel, String[] params) {
		channel.send().part("told by operator to leave");
	}

	@Override
	public String[] help() {
		return new String[] {
				"Used to make the bot leave the channel, can only be issued by OPs"
		};
	}

}

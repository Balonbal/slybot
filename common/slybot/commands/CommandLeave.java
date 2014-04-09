package slybot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;

import slybot.SlyBot;

public class CommandLeave extends Command {

	public CommandLeave() {
		super("leave", true, false, false);
	}

	@Override
	public void run(SlyBot bot, User user, Channel channel, String[] params) {
		if (params[0] == "-s") {
			if (CommandHandler.isBotOP(user)) {
				channel.send().message("SlyBot is shutting down forcibly.");
				System.exit(0); //TODO Create close method
			}
		}
		channel.send().part("told by operator to leave");
	}

	@Override
	public String[] help() {
		return new String[] {
				"Used to make the bot leave the channel, can only be issued by OPs"
		};
	}

	@Override
	public User challenge(SlyBot bot, User usera, User userb, Channel channel, String[] params) {
		return null;
	}

}

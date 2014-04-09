package slybot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;

import slybot.Main;
import slybot.SlyBot;
import slybot.core.Challenge;

public class CommandChallenge extends Command {

	public CommandChallenge() {
		super("challenge", false, false, false);
	}

	@Override
	public String[] help() {
		return null;
	}

	@Override
	public void run(SlyBot bot, User user, Channel channel, String[] params) {
		if (channel != null) {
			if (params.length >= 2) {
				Command cmd = CommandHandler.getCommand(params[1]);
				if (cmd != null && !params[0].equalsIgnoreCase(user.getNick())) {
					String[] newParams = new String[params.length - 2];
					for (int i = 2; i <= params.length -1; i++) {
						newParams[i-2] = params[i];
					}
					Main.getChallengeManager().cleanup();
					Main.getChallengeManager().addChallenge(channel, user, params[0], cmd, newParams);
				}
			}
		}
	}

	@Override
	public User challenge(SlyBot bot, User usera, User userb, Channel channel,
			String[] params) {
		// TODO Auto-generated method stub
		return null;
	}

}

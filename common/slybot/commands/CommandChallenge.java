package slybot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;

import slybot.Main;
import slybot.SlyBot;
import slybot.challenges.Challenge;
import slybot.challenges.ChallengeRTD;
import slybot.challenges.ChallengeTickTackToe;
import slybot.lib.Reference;

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
				
				if (!params[0].equalsIgnoreCase(user.getNick())) {
					String[] newParams = new String[params.length - 2];
					for (int i = 2; i <= params.length -1; i++) {
						newParams[i-2] = params[i];
					}
					
					Challenge c = null;
					switch(params[1]) {
					case "rtd":
						c = new ChallengeRTD(channel, user, params[0], newParams, Reference.ACCEPT_TIMEOUT);
						break;
					case "ttt":
						c = new ChallengeTickTackToe(channel, user, params[0], newParams, Reference.ACCEPT_TIMEOUT);
						break;
					} 

					if (c != null) {
						Main.getChallengeManager().addChallenge(c);
					}
				}
			}
		}
	}


}

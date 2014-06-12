package slybot.commands;

import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;

import slybot.Main;
import slybot.SlyBot;
import slybot.challenges.Challenge;
import slybot.challenges.ChallengeRTD;
import slybot.challenges.ChallengeTicTacToe;
import slybot.lib.Reference;

public class CommandChallenge extends Command {

	public CommandChallenge() {
		super("challenge", false, false, true);
	}

	@Override
	public String[] help() {
		return new String[] {
				"Used to start a challenge in one of the available games",
				"SYNTAX: " + Colors.BOLD + "CHALLENGE <user> <challenge>" + Colors.NORMAL,
				"Example: " + Colors.BOLD + "CHALLENGE sly ttt" + Colors.NORMAL,
				"Use " + Colors.BOLD + "CHALLENGE LIST" + Colors.NORMAL + " to get a list of available challenges"
		};
	}

	@Override
	public void run(SlyBot bot, User user, Channel channel, String[] params) {
		if (channel != null) {
			if (params[0].equalsIgnoreCase("list")) {
				for (String s: new String[] {
					"Available Challenges: ",
					"RTD -- Roll the Dice",
					"TTT - Tic Tac Toe"
				}) {
					channel.send().message(s);
				}
			}
			if (params.length >= 2) {
				
				if (!params[0].equalsIgnoreCase(user.getNick())) {
					String[] newParams = new String[params.length - 2];
					for (int i = 2; i <= params.length -1; i++) {
						newParams[i-2] = params[i];
					}
					
					Challenge c = null;
					switch(params[1].toLowerCase()) {
					case "rtd":
						c = new ChallengeRTD(channel, user, params[0], newParams, Reference.ACCEPT_TIMEOUT);
						break;
					case "ttt":
						c = new ChallengeTicTacToe(channel, user, params[0], newParams, Reference.ACCEPT_TIMEOUT);
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

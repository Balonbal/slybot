package com.balonbal.slybot.commands;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.challenges.Challenge;
import com.balonbal.slybot.challenges.ChallengeRTD;
import com.balonbal.slybot.challenges.ChallengeTicTacToe;
import com.balonbal.slybot.lib.Reference;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;

public class CommandChallenge implements Command {

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
	public void run(User user, Channel channel, String[] params) {
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
                    System.arraycopy(params, 2, newParams, 0, newParams.length);
                    /*for (int i = 2; i <= params.length -1; i++) {
                        newParams[i-2] = params[i];
					}*/
					
					Challenge c = null;
					if (params[1].equalsIgnoreCase("rtd")) {
                        c = new ChallengeRTD(channel, user, params[0], newParams, Reference.ACCEPT_TIMEOUT);
                    } else if (params[1].equalsIgnoreCase("ttt")) {
						c = new ChallengeTicTacToe(channel, user, params[0], newParams, Reference.ACCEPT_TIMEOUT);
					} 

					if (c != null) {
						Main.getChallengeManager().addChallenge(c);
					}
				}
			}
		}
	}

	@Override
	public String[] getTriggers() {
		return new String[] {
				"challenge",
				"chal"
		};
	}

	@Override
	public boolean requiresOP() {
		return false;
	}

	@Override
	public boolean channelCommand() {
		return true;
	}

	@Override
	public boolean pmCommand() {
		return false;
	}


}

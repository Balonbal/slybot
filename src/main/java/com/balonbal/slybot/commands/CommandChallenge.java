package com.balonbal.slybot.commands;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.challenges.Challenge;
import com.balonbal.slybot.challenges.ChallengeRTD;
import com.balonbal.slybot.challenges.ChallengeTicTacToe;
import com.balonbal.slybot.lib.Reference;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.MessageEvent;

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
	public void run(String[] params, Event<SlyBot> event) {
        if (!(event instanceof MessageEvent)) return;
        Channel channel = ((MessageEvent) event).getChannel();
        User user = ((MessageEvent) event).getUser();

		if (params[1].equalsIgnoreCase("list")) {
			for (String s: new String[] {
				"Available Challenges: ",
				"RTD -- Roll the Dice",
				"TTT - Tic Tac Toe"
			}) {
				Main.getBot().reply(channel, user, s);
			}
		}
		if (params.length >= 2) {

            User challengedUser = null;
            //Find the user linked to the specified username
            for (User u: channel.getUsers()) {
                if (u.getNick().equalsIgnoreCase(params[1])) challengedUser = u;
            }

            if (challengedUser == null) {
                event.getBot().reply(event, "User " + Colors.RED + params[1] + Colors.NORMAL + " was not found in this channel.");
                return;
            }

            //Do not create matches with the bot or themselves
			if (!user.equals(challengedUser) && !user.equals(event.getBot().getUserBot())) {
				String[] newParams = new String[params.length - 3];
                System.arraycopy(params, 3, newParams, 0, newParams.length);
                /*for (int i = 2; i <= params.length -1; i++) {
                    newParams[i-2] = params[i];
				}*/

				Challenge c = null;
				if (params[2].equalsIgnoreCase("rtd")) {
                    c = new ChallengeRTD(channel, user, challengedUser, newParams, Reference.ACCEPT_TIMEOUT);
                } else if (params[2].equalsIgnoreCase("ttt")) {
					c = new ChallengeTicTacToe(channel, user, challengedUser, newParams, Reference.ACCEPT_TIMEOUT);
				}

				if (c != null) {
				Main.getChallengeManager().addChallenge(c);
				}
			}
		}
	}

	@Override
	public String getTrigger() {
		return "challenge";
	}

	@Override
	public int requiresOP() {
		return Reference.REQUIRES_OP_NONE;
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

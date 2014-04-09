package slybot.core;

import java.util.ArrayList;

import org.pircbotx.Channel;
import org.pircbotx.User;

import slybot.commands.Command;

public class ChallengeManager {
	
	private ArrayList<Challenge> chal;
	
	public ChallengeManager() {
		chal = new ArrayList<Challenge>();
	}
	
	public void addChallenge(Channel channel, User user,String userb, Command cmd, String[] params) {
		for (int i = 0; i < chal.size(); i++) {
			if (chal.get(i).host.getNick().equalsIgnoreCase(user.getNick())) {
				return;
			}
			
		}
		Challenge c = new Challenge(channel, user, userb, cmd, params);
		channel.send().message(" Oh snap, " + c.host.getNick() + " challenged " + c.challengedUser + " to a duel of " + c.command.command +". Use the \"accept\" command to accept the challenge");
		chal.add(c);
		chal.get(chal.size()-1).timeOut();
	}
	
	public void removeChallenge(Challenge c) {
		for (int i = 0; i < chal.size(); i++) {
			if (chal.get(i) == c) {
				chal.remove(i);
			}
		}
	}
	
	public void tryAccept(User u) {
		for (Challenge c: chal) {
			c.tryAccept(u);
		}
	}
	
	public void cleanup() {
		for (int i = 0; i < chal.size(); i++) {
			if (chal.get(i).completed) {
				chal.remove(i);
			}
		}
	}

}

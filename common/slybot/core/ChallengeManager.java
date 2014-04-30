package slybot.core;

import java.util.ArrayList;

import org.pircbotx.User;

import slybot.challenges.Challenge;
import slybot.challenges.MultiTurnChallenge;

public class ChallengeManager {
	
	
	private ArrayList<Challenge> chal;
	
	public ChallengeManager() {
		chal = new ArrayList<Challenge>();
	}
	
	public void addChallenge(Challenge c) {
		for (int i = 0; i < chal.size(); i++) {
			//Do not add challenges to users that already have one going in the SAME channel
			if ((chal.get(i).getHost().getNick().equalsIgnoreCase(c.getHost().getNick()) || chal.get(i).getChallengedUser().equalsIgnoreCase(c.getChallengedUser())) && chal.get(i).getChannel().getChannelId().equals(c.getChannel().getChannelId())) {
				return;
			}
			
		}
		c.getChannel().send().message("Oh snap, " + c.getHost().getNick() + " challenged " + c.getChallengedUser() + " to a duel of " + c.getDescription()+". Use the \"accept\" command to accept the challenge");
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
	
	public void doNextTurn(User u, String[] params) {
		for (int i=0;i < chal.size(); i++) {
			if (chal.get(i) instanceof MultiTurnChallenge) {
				MultiTurnChallenge c = (MultiTurnChallenge) chal.get(i);
				c.doTurn(u, params);
			}
		}
	}

}

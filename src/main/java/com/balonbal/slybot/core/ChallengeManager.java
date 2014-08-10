package com.balonbal.slybot.core;

import com.balonbal.slybot.challenges.Challenge;
import org.pircbotx.User;

import java.util.ArrayList;
import java.util.logging.Logger;

public class ChallengeManager {

    private static final Logger logger = Logger.getLogger(ChallengeManager.class.getName());

    private ArrayList<Challenge> chal;

    public ChallengeManager() {
		chal = new ArrayList<Challenge>();
	}
	
	public void addChallenge(Challenge c) {
        for (Challenge challenge : chal) {
            if (challenge.completed) {
                removeChallenge(challenge);
            }
			//Do not add challenges to users that already have one going in the SAME channel
            if ((challenge.getHost().getNick().equalsIgnoreCase(c.getHost().getNick()) || challenge.getChallengedUser().equalsIgnoreCase(c.getChallengedUser())) && challenge.getChannel().getName().equals(c.getChannel().getName())) {
                return;
			}
			
		}
        logger.info("Started challenge between user " + c.getHost().getNick() + " (host) and " + c.getChallengedUser());
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
			if (u.getNick().equalsIgnoreCase(c.getChallengedUser())) {
				c.initialize();
			}
		}
	}
	
	public void doNextTurn(User u, String[] params) {
		for (Challenge c: chal) {
			c.proccessTurn(u, params);
		}
	}

}

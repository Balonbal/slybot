package com.balonbal.slybot.core;

import com.balonbal.slybot.challenges.Challenge;
import org.pircbotx.User;

import java.util.ArrayList;

public class ChallengeManager {

    private ArrayList<Challenge> chal;

    public ChallengeManager() {
		chal = new ArrayList<Challenge>();
	}
	
	public void addChallenge(Challenge c) {
        System.out.println("Trying to create a new challenge");
        ArrayList<Challenge> removeChallenges = new ArrayList<Challenge>();
        for (Challenge challenge : chal) {
            System.out.println(challenge.completed + "");
            if (challenge.completed) {
                removeChallenges.add(challenge);
            //Do not add challenges to users that already have one going in the SAME channe
            } else if ((challenge.getHost().getNick().equalsIgnoreCase(c.getHost().getNick()) || challenge.getChallengedUser().equals(c.getChallengedUser())) && challenge.getChannel().getName().equals(c.getChannel().getName())) {
                System.out.println("Challenge not initialized as one of the user already has a challenge.");
                return;
			}
		}

        //Remove obsolete challenges
        for (Challenge challenge: removeChallenges) {
            chal.remove(challenge);
        }

        System.out.println("Started challenge between user " + c.getHost().getNick() + " (host) and " + c.getChallengedUser().getNick());
        c.getChannel().send().message("Oh snap, " + c.getHost().getNick() + " challenged " + c.getChallengedUser().getNick() + " to a duel of " + c.getDescription()+". Use the \"accept\" command to accept the challenge.");
		chal.add(c);
		chal.get(chal.size()-1).timeOut();
	}

    public void removeChallenge(Challenge c) {
        chal.remove(c);
    }
	
	public void tryAccept(User u) {
		for (Challenge c: chal) {
			if (u.equals(c.getChallengedUser()) && !c.completed) {
				c.initialize();
			}
		}
	}
	
	public void doNextTurn(User u, String[] params) {
		for (Challenge c: chal) {
            if (!c.completed) {
                c.proccessTurn(u, params);
            }
		}
	}

}

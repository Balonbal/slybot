package slybot.core;

import java.util.Timer;

import org.pircbotx.Channel;
import org.pircbotx.User;

import slybot.Main;
import slybot.SlyBot;
import slybot.commands.Command;

public class Challenge {
	
	Channel channel;
	User host;
	String challengedUser;
	Command command;
	String[] params;
	public boolean completed = false;
	public long timeOut;
	
	public Challenge(Channel chan, User initializer, String challenged, Command cmd, String[] parameters) {
		channel = chan;
		host = initializer;
		challengedUser = challenged;
		command = cmd;
		params = parameters;
		timeOut = System.currentTimeMillis() + 30000;
	}
	
	public void timeOut() {
		while (System.currentTimeMillis() < timeOut) {
			try {
				Thread.sleep(1000);
				System.out.println("time left: " + (timeOut - System.currentTimeMillis()));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		channel.send().message("Challenge timed out, aborting");
		completed = true;
		Main.getChallengeManager().removeChallenge(this);
	}
	
	
	public void tryAccept(User playah) {
		System.out.println(timeOut + " | " + completed + " | " + System.currentTimeMillis());
		if (completed) {
			System.out.println("Challenge already accepted");
			Main.getChallengeManager().removeChallenge(this);
		}else {
			if (playah.getNick().equalsIgnoreCase(challengedUser)) {
				
				System.out.println("Challenge accepted");
				
				//System.out.println(params[0]);
				
				System.out.println("Checkpoint a");
				channel.send().message(challengedUser + " accepted challenge from " + host.getNick());
				command.challenge((SlyBot) host.getBot(), host, playah, channel, params);
				Main.getChallengeManager().removeChallenge(this);
			} else {
				System.out.println("wrong nick");
			}
		}
	}

}

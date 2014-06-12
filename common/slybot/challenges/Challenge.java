package slybot.challenges;

import org.pircbotx.Channel;
import org.pircbotx.User;

import slybot.Main;
import slybot.SlyBot;

public abstract class Challenge {
	
	private Channel channel;
	private User host;
	private String challengedUser;
	private String name;
	String[] params;
	public boolean completed = false;
	public long timeOut;
	
	public Challenge(Channel chan, User initializer, String challenged, String[] parameters, int timeOutInSeconds) {
		channel = chan;
		host = initializer;
		challengedUser = challenged;
		params = parameters;
		
		//Set the timeout to 30 seconds after the creation time
		timeOut = System.currentTimeMillis() + timeOutInSeconds * 1000;
	}
	
	public void timeOut() {
		
		//While the current time has not passed the 
		while (System.currentTimeMillis() < timeOut && !completed) {
			try {
				//Sleep for a second
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (!completed)  {
			//inform the channel that the challenge was aborted and remove the challenge from the manager
			getChannel().send().message("Challenge timed out, aborting");
			completed = true;
			Main.getChallengeManager().removeChallenge(this);
		}
	}
	
	
	public void tryAccept(User playah) {
		if (completed) {
			Main.getChallengeManager().removeChallenge(this);
		}else {
			if (playah.getNick().equalsIgnoreCase(getChallengedUser())) {
				User u = run((SlyBot) getHost().getBot(), getHost(), playah, getChannel(), params);
				completed = true;
				//Stop the challenge
				end(u.getNick());
				Main.getChallengeManager().removeChallenge(this);
			}
		}
	}
	
	public abstract User run(SlyBot bot, User usera, User userb, Channel channel, String[] params);
	
	public abstract void initialize();

	/**
	 * @return the host
	 */
	public User getHost() {
		return host;
	}

	public String getChallengedUser() {
		return challengedUser;
	}

	/**
	 * @return the command
	 */
	public String getCommand() {
		return name;
	}

	/**
	 * @return the channel
	 */
	public Channel getChannel() {
		return channel;
	}
	
	public abstract String getDescription();

	public void end(String winner) {
		
	}


}

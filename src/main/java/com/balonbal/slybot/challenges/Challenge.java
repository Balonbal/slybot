package com.balonbal.slybot.challenges;

import com.balonbal.slybot.Main;
import org.pircbotx.Channel;
import org.pircbotx.User;

public abstract class Challenge {
	
	protected Channel channel;
	private User host;
	private User challengedUser;
	String[] params;
	public boolean completed = false;
	private long timeOut;
	
	public Challenge(Channel chan, User initializer, User challenged, String[] parameters, int timeOutInSeconds) {
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
            Main.getBot().reply(channel, null, "Challenge timed out.");
			completed = true;
		}
	}
	
	public void addTime(int timeInSeconds) {
		timeOut += timeInSeconds*1000;
	}
	
	public abstract void run();
	
	public abstract void initialize();

	/**
	 * @return the host
	 */
	public User getHost() {
		return host;
	}

	public User getChallengedUser() {
		return challengedUser;
	}

	/**
	 * @return the channel
	 */
	public Channel getChannel() {
		return channel;
	}
	
	public abstract String getDescription();

	public abstract void proccessTurn(User u, String[] params2);


}

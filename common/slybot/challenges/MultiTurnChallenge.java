package slybot.challenges;

import org.pircbotx.Channel;
import org.pircbotx.User;

import slybot.Main;

public abstract class MultiTurnChallenge extends Challenge {

	private int nextTurnTimeout;
	
	public MultiTurnChallenge(Channel chan, User initializer,
			String challenged, String name, String[] parameters,
			int timeOutInSeconds, int nextTurnTimeout) {
		super(chan, initializer, challenged, parameters, timeOutInSeconds);
		this.nextTurnTimeout = nextTurnTimeout;
	}
	
	public void proccessTurn(User u, String[] params) {
		System.out.println("doing turn for " + u.getNick() + ": " + params[0] + ", " + params[1]);
		//reset the clock
		if (doTurn(u, params)) {
			this.timeOut = System.currentTimeMillis() + nextTurnTimeout*1000;
		}
		
	}
	
	@Override
	public void tryAccept(User playah) {
		if (completed) {
			Main.getChallengeManager().removeChallenge(this);
		}else {
			if (playah.getNick().equalsIgnoreCase(getChallengedUser())) {
				//Reset the clock and try the command
				this.timeOut = System.currentTimeMillis() + this.nextTurnTimeout*1000;
				initialize();
			}
		}
	}
	
	public abstract boolean doTurn(User u, String[] params);

	public abstract String getResults();
}

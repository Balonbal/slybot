package com.balonbal.slybot.challenges;

import java.util.Random;

import com.balonbal.slybot.Main;
import org.pircbotx.Channel;
import org.pircbotx.User;

public class ChallengeRTD extends Challenge {
	
	public ChallengeRTD(Channel chan, User initializer, User challenged,  String[] parameters, int timeOutInSeconds) {
		super(chan, initializer, challenged, parameters, timeOutInSeconds);
		// TODO Auto-generated constructor stub
	}
	
	private long[] calculateResult(String[] params) {
		int max = 100;
		int min = 0;
		long num = 0;
		Random r = new Random();

		
		switch (params.length) {
		case 1:
			if (params[0] != null && params[0] != "null") {
				max = Integer.parseInt(params[0]);
			}
			break;
		case 2:
			if(params[0] != null && params[1] != null) {
				min = Integer.parseInt(params[0]);
				max = Integer.parseInt(params[1]);
			}
			break;
		}
		

		if (max > 0) {
			num = r.nextInt(max - min) + min + 1;
		}
		
		return new long[] { num, min, max };
	}

	@Override
	public void run() {
		
		//calculate the result for each player
		long[] resulta = calculateResult(params);
		long[] resultb = calculateResult(params);
		getChannel().send().message(getChallengedUser().getNick() + " has accepted a challenge from " + getHost().getNick() + " in " + (resulta[2] - resulta[1]) + " sided dicing.");
		channel.send().message("The fight is on! " + getHost().getNick() + " rolls... " + resulta[0] + " versus " + getChallengedUser().getNick() + ", who rolls... " + resultb[0] + "!");
		
		if (resulta[0] > resultb[0]) {
			Main.getBot().reply(channel, null, getHost().getNick() + " is rolling all over " + getChallengedUser().getNick());
			completed = true;
		} else if (resulta[0] == resultb[0]) {
            Main.getBot().reply(channel, null, "The dices are fighting back! It's a tie!");
			completed = true;
		} else {
            Main.getBot().reply(channel, null, getHost().getNick() + " is having a bad day, as " + getChallengedUser().getNick() + " just defated his dice!");
			completed = true;
		}
        Main.getChallengeManager().removeChallenge(this);

    }

	@Override
	public void initialize() {
		run();
	}

	@Override
	public String getDescription() {
		return "dice throwing";
	}

	@Override
	public void proccessTurn(User u, String[] params2) {
		//does not do turns
	}

}

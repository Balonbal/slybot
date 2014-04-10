package slybot.challenges;

import java.util.Random;

import org.pircbotx.Channel;
import org.pircbotx.User;

import slybot.SlyBot;

public class ChallengeRTD extends Challenge {
	
	public ChallengeRTD(Channel chan, User initializer, String challenged,  String[] parameters, int timeOutInSeconds) {
		super(chan, initializer, challenged, parameters, timeOutInSeconds);
		// TODO Auto-generated constructor stub
	}
	
	private long[] calculateResult(String[] params) {
		int max = 100;
		int min = 0;
		long num = 0;
		Random r = new Random();
		
		System.out.println(params.length);
		
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
	public User run(SlyBot bot, User usera, User userb, Channel channel,
			String[] params) {
		
		
		long[] resulta = calculateResult(params);
		long[] resultb = calculateResult(params);
		channel.send().message(userb.getNick() + " has accepted a challenge from " + usera.getNick() + " in " + (resulta[2] - resulta[1]) + " sided dicing.");
		channel.send().message("The fight is on! " + usera.getNick() + " rolls... " + resulta[0] + " versus " + userb.getNick() + ", who rolls... " + resultb[0] + "!");
		
		if (resulta[0] > resultb[0]) {
			channel.send().message(usera.getNick() + " is rolling all over " + userb.getNick());
			return usera;
		} else if (resulta[0] == resultb[0]) {
			channel.send().message("The dices are fighting back! It's a tie!");
			return null;
		} else {
			channel.send().message(usera.getNick() + " is having a bad day, as " + userb.getNick() + " just defated his dice!");
			return userb;
		}
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDescription() {
		return "dice throwing";
	}

}

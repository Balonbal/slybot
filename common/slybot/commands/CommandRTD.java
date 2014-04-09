package slybot.commands;

import java.util.Random;

import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;

import slybot.SlyBot;

public class CommandRTD extends Command {

	public CommandRTD() {
		super("rtd", false, false, true);
	}

	@Override
	public String[] help() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run(SlyBot bot, User user, Channel channel, String[] params) {
		System.out.println(params.length);
		long[] numbers = calculateResult(params);
		broadcastResult(channel, user, (int) numbers[1], (int) numbers[2], numbers[0]);
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
		
	public void broadcastResult(Channel channel, User user, int min, int max, long num) {
		if (channel != null && (num > 0)) {
			channel.send().message(user.getNick() + " rolls the dice (" + (max-min) + " sides) and got... " + num + "!");
		}
		
	}

	@Override
	public User challenge(SlyBot bot, User usera, User userb, Channel channel, String[] params) {
		System.out.println("rolling rtd challenge");
		long[] resulta = calculateResult(params);
		long[] resultb = calculateResult(params);
		System.out.println(resulta[0]);
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
	}

}

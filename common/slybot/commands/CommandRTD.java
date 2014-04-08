package slybot.commands;

import java.util.Random;

import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;

import slybot.SlyBot;

public class CommandRTD extends Command {

	public CommandRTD() {
		super("rtd", false, false);
	}

	@Override
	public String[] help() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run(SlyBot bot, User user, Channel channel, String[] params) {
		int max = 100;
		int min = 0;
		long num = 0;
		Random r = new Random();
		
		switch (params.length) {
		case 1:
			max = Integer.parseInt(params[0]);
			break;
		case 2:
			min = Integer.parseInt(params[0]);
			max = Integer.parseInt(params[1]);
			break;
		}
		

		if (max > 0) {
			num = r.nextInt(max - min) + min + 1;
		}
		
		if (channel != null && (num > 0)) {
			channel.send().message(user.getNick() + " rolls the dice (" + (max-min) + " sides) and got... " + num + "!");
		}
		
	}

}

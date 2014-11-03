package com.balonbal.slybot.commands;

import java.util.Random;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.lib.Reference;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

public class CommandRTD implements Command {

	@Override
	public String[] help() {
		return new String[] {
				"Used to roll a dice.",
				"Syntax: " + Colors.BOLD + "RTD [HIGHEST]" + Colors.NORMAL,
				"OR " + Colors.BOLD + "RTD [LOWEST] [HIGHEST]" + Colors.NORMAL + " if you want more control",
				"Defaults to " + Colors.BOLD + "RTD 0 100" + Colors.NORMAL
		};
	}

	@Override
	public void run(String[] params, Event<SlyBot> event) {
		long[] numbers = calculateResult(params);
		broadcastResult(event, (int) numbers[1], (int) numbers[2], numbers[0]);
	}
	
	private long[] calculateResult(String[] params) {
		int max = 100;
		int min = 0;
		long num = 0;
		Random r = new Random();
		
		
		switch (params.length) {
		case 2:
			if (params[1] != null && params[1] != "null") {
				max = Integer.parseInt(params[1]);
			}
			break;
		case 3:
			if(params[1] != null && params[2] != null) {
				min = Integer.parseInt(params[1]);
				max = Integer.parseInt(params[2]);
			}
			break;
		}
		

		if (max > 0) {
			num = r.nextInt(max - min) + min + 1;
		}
		
		return new long[] { num, min, max };
	}
		
	public void broadcastResult(Event<SlyBot> event, int min, int max, long num) {
        User user = null;
        if (event instanceof MessageEvent) user = ((MessageEvent) event).getUser();
        if (event instanceof PrivateMessageEvent) user = ((PrivateMessageEvent) event).getUser();

        event.getBot().reply(event, user.getNick() + " rolls the dice (" + (max - min) + " sides) and got... " + num + "!");
		
	}

	@Override
	public String getTrigger() {
		return "rtd";
	}

	@Override
	public int requiresOP() {
		return Reference.REQUIRES_OP_NONE;
	}

	@Override
	public boolean channelCommand() {
		return true;
	}

	@Override
	public boolean pmCommand() {
		return false;
	}

}

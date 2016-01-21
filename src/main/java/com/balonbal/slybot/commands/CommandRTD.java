package com.balonbal.slybot.commands;

import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.lib.Reference;
import org.pircbotx.Colors;
import org.pircbotx.User;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import java.util.Random;

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
	public String run(String[] params, Event<SlyBot> event) {
		int max = 100;
		int min = 0;
		long num;
		Random r = new Random();

		try {
			switch (params.length) {
				case 1:
					break;
				case 2:
					if (params[1] != null && params[1] != "null") {
						max = Integer.parseInt(params[1]);
					}
					break;
				case 3:
					if (params[1] != null && params[2] != null) {
						min = Integer.parseInt(params[1]);
						max = Integer.parseInt(params[2]);
					}
					break;
				default:
					return randomizeList(event, params);
			}
		} catch (NumberFormatException e) {
			return randomizeList(event, params);
		}

		//Fix wrong order
		if (max < min) {
			int tempMax = max;
			max = min;
			min = tempMax;
		}
		

		num = r.nextInt(max - min) + min + 1;

		User user = null;
		if (event instanceof MessageEvent) user = ((MessageEvent) event).getUser();
		if (event instanceof PrivateMessageEvent) user = ((PrivateMessageEvent) event).getUser();

		event.getBot().reply(event, user.getNick() + " rolls the dice (" + (max - min) + " side" + (max - min == 1 ? "" : "s") + ") and got... " + num + "!");

		return  num + "";
	}

	public String randomizeList(Event<SlyBot> event, String[] params) {
		Random r = new Random();

		String result = params[r.nextInt(params.length -1) + 1];

		User user = null;
		if (event instanceof MessageEvent) user = ((MessageEvent) event).getUser();
		if (event instanceof PrivateMessageEvent) user = ((PrivateMessageEvent) event).getUser();

		event.getBot().reply(event, user.getNick() + " rolls a very special dice.. It shows \"" + result + "\"");
		return result;
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
		return true;
	}

}

package com.balonbal.slybot.commands;

import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.lib.Reference;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.core.CommandHandler;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

public class CommandLeave implements Command {

	@Override
	public void run(String[] parameters, Event<SlyBot> event) {

        Channel channel = null;
        User user = null;
        if (event instanceof MessageEvent) {
            user = ((MessageEvent) event).getUser();
            channel = ((MessageEvent) event).getChannel();
        }

		if (parameters.length > 1) {
			if (parameters[1].equals("-s")) {
				//Check for botOP NOTE: this is not channel-op
				if (event.getBot().isBotOP(user)) {
					event.getBot().reply(event, "SlyBot is shutting down.");
					//Shutdown the bot
					event.getBot().shutdown();
				} else {
                    event.getBot().reply(event, "You may not turn off the bot, sorry.");
                    return;
                }
			} else {
                for (Channel c: event.getBot().getUserBot().getChannels()) {
                    if (c.getName().equalsIgnoreCase(parameters[1])) channel = c;
                }
            }
		}

        if (channel == null) {
            Main.getBot().reply(event, "Could not leave channel: Not Found");
            return;
        }

		System.out.println("Leaving channel: " + channel.getName());
		//Else leave the channel
		channel.send().part("told by operator to leave");
	}

	@Override
	public String[] help() {
		return new String[] {
				"Used to make the bot leave the channel, can only be issued by OPs",
				"SYNTAX: " + Colors.BOLD + "LEAVE [-s]",
				"-s        Shut down the bot"
		};
	}

	@Override
	public String getTrigger() {
		return "leave";
	}

	@Override
	public int requiresOP() {
		return Reference.REQUIRES_OP_ANY;
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

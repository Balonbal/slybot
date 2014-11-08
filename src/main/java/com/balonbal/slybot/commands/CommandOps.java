package com.balonbal.slybot.commands;

import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.lib.Reference;
import org.pircbotx.Colors;

import com.balonbal.slybot.lib.Settings;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.PrivateMessageEvent;

public class CommandOps implements Command {

	@Override
	public String[] help() {
		return new String[] {
				"Used to list, add and/or remove operators for this bot. Does " + Colors.BOLD + "NOT" + Colors.NORMAL + " affect any channels",
				"Can only be issued by bot-operators per PM",
				"Will return a list of the current operators if no parameters are passed",
				"SYNTAX: " + Colors.BOLD + "OPS [ <ADD|REMOVE> <user> <botpassword> ]" + Colors.NORMAL,
				"Example: " + Colors.BOLD + "OPS ADD sly mySecretPassword123" + Colors.NORMAL
		};
	}

	@Override
	public String run(String[] params, Event<SlyBot> event) {
		//Double check that this is in fact requested through a pm
        if (!(event instanceof PrivateMessageEvent)) return "false";

		if (params.length > 3) {
            String nick = params[2];
            String pwd = params[3];


            if (pwd.equals(Settings.operatorpass)) {
                if (params[1].equalsIgnoreCase("add")) {
                    event.getBot().getConfig().appendSetting(Reference.CONFIG_BOTOPS, nick);
                } else if (params[1].equalsIgnoreCase("remove")) {
                    //Owner cannot be removed from the ops
                    if (!nick.equalsIgnoreCase(Settings.owner)) {
                        event.getBot().getConfig().removeSetting(Reference.CONFIG_BOTOPS, nick);
                    }
                }
            }
        }
		event.getBot().reply(event, "The current bot operators are: ");
		for (String s: Settings.botops) {
			event.getBot().reply(event, " - " + s);
		}
		event.getBot().reply(event, "Total: " + Settings.botops.size() + " items");

        return "true";
	}

	@Override
	public String getTrigger() {
		return "ops";
	}

	@Override
	public int requiresOP() {
		return Reference.REQUIRES_OP_BOT;
	}

	@Override
	public boolean channelCommand() {
		return false;
	}

	@Override
	public boolean pmCommand() {
		return true;
	}

}

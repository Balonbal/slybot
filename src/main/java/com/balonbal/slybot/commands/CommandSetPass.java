package com.balonbal.slybot.commands;

import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.lib.Reference;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.lib.Settings;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.PrivateMessageEvent;

public class CommandSetPass implements Command {

	@Override
	public String[] help() {
		return new String[] {
				"Set the password for operator commands.",
				"Will require the current password if one is set.",
				"SYNTAX: " + Colors.BOLD + "setpass [current password] <new password>" + Colors.NORMAL
		};
	}

	@Override
	public void run(String[] params, Event<SlyBot> event) {
        if (!(event instanceof PrivateMessageEvent)) return;

		if (params.length == 2) {
			//Check if no password is set
			if (Settings.operatorpass.equals("")) {
                event.getBot().getConfig().updateSetting(Reference.CONFIG_BOTPASS, params[1]);
				event.getBot().reply(event, "Sucessfully updated password.");
			}
		} else if (params.length == 3){
			if (Settings.operatorpass.equals(params[1])) {
                event.getBot().getConfig().updateSetting("operatorpass", params[2]);
                event.getBot().reply(event, "Sucessfully updated password.");
			}
		}
	}

	@Override
	public String getTrigger() {
		return "setpass";
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

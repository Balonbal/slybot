package com.balonbal.slybot.commands;

import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.lib.Reference;
import org.pircbotx.Channel;
import org.pircbotx.User;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.lib.Settings;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import java.util.ArrayList;

public class CommandClaim implements Command {

	@Override
	public String[] help() {
		return new String[] {
				"Used to initialy gain control over the bot. You only need to do this once."
		};
	}

	@Override
	public String run(String[] params, Event<SlyBot> event) {

		if (!(event instanceof PrivateMessageEvent)) return "false";
        User user = ((PrivateMessageEvent) event).getUser();
		
		if (Settings.owner.equals("")) {
			//check if the user is verified with nickserv
			if (user.isVerified()) {
				//update config accordingly
			    event.getBot().getConfig().updateSetting(Reference.CONFIG_OWNER, user.getNick());
                ArrayList<String> list  = new ArrayList<String>();
                list.add(user.getNick());
				event.getBot().getConfig().updateSetting(Reference.CONFIG_BOTOPS, list);
				event.getBot().reply(event, "Successfully claimed bot.");
                return "true";
			} else {
				user.send().message("Please register your nick before claiming this bot");
			}
			
		} else if (Settings.owner.equalsIgnoreCase(user.getNick())){
		    event.getBot().reply(event, "You already own this bot, silly");
		} else {
			event.getBot().reply(event, "Error! Bot already claimed by " + Settings.owner + ".");
		}
        return "false";
	}

	@Override
	public String getTrigger() {
		return "claim";
	}

	@Override
	public int requiresOP() {
        return Reference.REQUIRES_OP_NONE;
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

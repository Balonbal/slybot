package com.balonbal.slybot.commands;

import com.balonbal.slybot.lib.Reference;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import com.balonbal.slybot.Main;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.MessageEvent;

public class CommandAccept implements Command {

	@Override
	public String[] help() {
		return new String[] {
				"Used to accept a challenge initiated by another user"
		};
	}

    @Override
    public String run(String[] parameters, Event event) {
        if (!(event instanceof MessageEvent)) return "";

        MessageEvent e = (MessageEvent) event;

        Main.getChallengeManager().tryAccept(e.getUser());
        return "";
    }

	@Override
	public String getTrigger() {
		return "accept";
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

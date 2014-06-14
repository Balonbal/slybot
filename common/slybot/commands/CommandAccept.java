package slybot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;

import slybot.Main;

public class CommandAccept implements Command {

	@Override
	public String[] help() {
		return new String[] {
				"Used to accept a challenge initiated by another user"
		};
	}

	@Override
	public void run(User user, Channel channel, String[] params) {
		System.out.println(user.getNick() + " accepted challenge");
		//Main.getChallengeManager().cleanup();
		Main.getChallengeManager().tryAccept(user);
	}

	@Override
	public String[] getTriggers() {
		return new String[] {
			"accept"	
		};
	}

	@Override
	public boolean requiresOP() {
		return false;
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

package slybot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;

import slybot.Main;
import slybot.SlyBot;

public class CommandAccept extends Command {

	public CommandAccept() {
		super("accept", false, false, true);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] help() {
		return new String[] {
				"Used to accept a challenge initiated by another user"
		};
	}

	@Override
	public void run(SlyBot bot, User user, Channel channel, String[] params) {
		System.out.println(user.getNick() + " accepted challenge");
		//Main.getChallengeManager().cleanup();
		Main.getChallengeManager().tryAccept(user);
	}

}

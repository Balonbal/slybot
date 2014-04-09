package slybot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;

import slybot.Main;
import slybot.SlyBot;

public class CommandAccept extends Command {

	public CommandAccept() {
		super("accept", false, false, false);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] help() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run(SlyBot bot, User user, Channel channel, String[] params) {
		System.out.println(user.getNick() + " accepted challenge");
		//Main.getChallengeManager().cleanup();
		Main.getChallengeManager().tryAccept(user);
	}

	@Override
	public User challenge(SlyBot bot, User usera, User userb, Channel channel,
			String[] params) {
		// TODO Auto-generated method stub
		return null;
	}

}

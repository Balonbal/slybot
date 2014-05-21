package slybot.commands;

import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;

import slybot.Main;
import slybot.SlyBot;
import slybot.core.SlyConfiguration;
import slybot.lib.Settings;

public class CommandOps extends Command {

	public CommandOps() {
		super("ops", true, true, false);
		// TODO Auto-generated constructor stub
	}


	@Override
	public String[] help() {
		return new String[] {
				"Used to list, add and/or remove operators for this bot. Does " + Colors.BOLD + "NOT" + Colors.NORMAL + " affect any channels",
				"Can only be issued by bot-operators per PM",
				"Will return a list of the current operators if no parameters are passed",
				"SYNTAX: " + Colors.BOLD + "OPS [ADD|REMOVE] [user] [botpassword]" + Colors.NORMAL,
				"Example: " + Colors.BOLD + "OPS ADD sly mySecretPassword123" + Colors.NORMAL
		};
	}

	@Override
	public void run(SlyBot bot, User user, Channel channel, String[] params) {
		//Double check that this is in fact requested through a pm
		if (channel == null) {
			if (params.length > 2) {
				String nick = params[1];
				String pwd = params[2];
				
				if (pwd.equals(Settings.operatorpass)) {
					switch (params[0]) {
					case "add":
						Main.getConfig().appendSetting("botops", ",", nick);
						break;
					case "remove":
						//Owner cannot be removed from the ops
						if (!nick.equalsIgnoreCase(Settings.owner)) {
							Main.getConfig().changeSetting("botops", SlyConfiguration.getSetting("botops").replace("," + nick, ""));
						}
						break;
					}
				}
			}
			user.send().message("The current bot operators are: ");
			for (String s: Settings.botops) {
				user.send().message(" - " + s);
			}
			user.send().message("Total: " + Settings.botops.length + " items");
		}

	}

}

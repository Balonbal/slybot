package com.balonbal.slybot.core;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.commands.Command;
import com.balonbal.slybot.lib.Settings;
import org.apache.commons.lang3.StringUtils;
import org.pircbotx.Channel;
import org.pircbotx.User;

import java.util.logging.Logger;

public class CommandHandler {

    private static final Logger logger = Logger.getLogger(CommandHandler.class.getName());
    private static final Logger commandLogger = Logger.getLogger("commandLogger");

    public static void processCommand(User user, Channel channel, String message) {
        String cmd;
		String[] params;
        boolean isOP = (isBotOP(user) || (channel != null && isChannelOP(user, channel)));
		
		//If the message has parameters
		if (message.contains(" ")) {
			cmd = message.substring(0, message.indexOf(" "));
			message = message.substring(message.indexOf(" ")+1);

			//This will split at blank spaces, tabs etc
			params = message.split("\\s+");
		} else {
			cmd = message;
			params = new String[] { };
		}

        logger.info(String.format("%s %s tried to issue command: %s", (isOP ? "Operator" : "User"), user.getNick(), cmd));

        Command c = getCommand(cmd);
		if (c != null) {
			//If the command requires op, check for OP
			if (!(c.requiresOP() && !isOP) || isBotOP(user)) {
				//Only do commands in appropriate channels.
				if ((c.channelCommand() && channel != null) || (c.pmCommand() && channel == null) || c.channelCommand() && c.pmCommand()) {
                    commandLogger.info(String.format("Issuing command \"%s\" by user %s %s", cmd + " " + StringUtils.join(params, " "), user.getNick(), (channel != null ? "in channel " + channel.getName() : "")));
                    c.run(user, channel, params);
				}
			}
		}

        runAlias(user, channel, cmd, params);

	}
	
	public static boolean isBotOP(User user) {
		//Only include users verified by the nickserv
		if (user.isVerified()) {
			for (String s: Settings.botops) {
				if (s.equalsIgnoreCase(user.getNick())) {
					return true;
				}
			}
			
		}
		return false;
	}

   public static boolean isChannelOP(User u, Channel c) {
       return c.getOps().contains(u);
   }
	
	public static Command getCommand(String cmd) {
        for (Command c : Main.getCommandListener().getCommands()) {
            for (String trigger: c.getTriggers()) {
				if (cmd.equalsIgnoreCase(trigger)) {
					return c;
				}
			}
		}
		
		return null;
	}

    private static void runAlias(User u, Channel c, String alias, String[] params) {
        if (Settings.aliases.containsKey(alias)) {
            String command = Settings.aliases.get(alias);
            if (command.contains("$USER")) {
                command = command.replaceAll("\\$USER", u.getNick());
            }
            if (command.contains("$CHANNEL")) {
                command = command.replaceAll("\\$CHANNEL", c.getName());
            }
            int i = 1;
            while (command.contains("$" + i)) {
                command = command.replaceAll("\\$" + i, params[i-1]);
                i++;
            }



            processCommand(u, c, command);
        }
    }

}

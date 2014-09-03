package com.balonbal.slybot.commands;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.core.CommandHandler;
import com.balonbal.slybot.lib.Settings;
import org.apache.commons.lang3.StringUtils;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;

public class CommandAlias implements Command {
    @Override
    public String[] getTriggers() {
        return new String[] {
                "alias",
                "addcommand"
        };
    }

    @Override
    public boolean requiresOP() {
        return true;
    }

    @Override
    public boolean channelCommand() {
        return true;
    }

    @Override
    public boolean pmCommand() {
        return true;
    }

    @Override
    public String[] help() {
        return new String[] {
                "Creates a new alias to make custom commands. Can only be used by operators.",
                "SYNTAX: " + Colors.BOLD + "ALIAS <name> <command ... parameters>",
                "SPECIAL STRINGS: $1, $2 etc. = parameters, $USER = user issuing alias, $CHANNEL = channel where alias was issued. Escaped with a \"\\\"",
                "EXAMPLE: " + Colors.BOLD + "ALIAS Welcome SAY Welcome $1 to $CHANNEL." + Colors.NORMAL + "Will generate the alias \"" + Colors.BOLD + "welcome" + Colors.NORMAL + "\" that will greet anything passed as the first parameter",
                "NOTE: By default, all aliases are locked as op-only, a way to change the permissions is comming."
        };
    }

    @Override
    public void run(User user, Channel channel, String[] params) {
        if (!Settings.aliases.containsKey(params[0]) && (CommandHandler.getCommand(params[0]) == null)) {
            String name = params[0];
            String command = StringUtils.join(params, " ").substring(name.length());
            Main.getConfig().appendSetting("aliases", ",", params[0] + ",\"" + command + "\"");
            if (channel != null) {
                channel.send().message("Successfully bound alias " + Colors.BOLD + Colors.BLUE + name + Colors.NORMAL + " to " + Colors.BOLD + Colors.GREEN + command);
            } else {
                user.send().message("Successfully bound alias " + Colors.BOLD + Colors.BLUE + name + Colors.NORMAL + " to " + Colors.BOLD + Colors.GREEN + command);
            }
        } else {
            if (channel != null) {
                channel.send().message("Could not bind " + Colors.BOLD + Colors.RED + params[0] + Colors.NORMAL + " as it is already in use.");
            } else {
                user.send().message("Could not bind " + Colors.BOLD + Colors.RED + params[0] + Colors.NORMAL + " as it is already in use.");
            }
        }
    }
}

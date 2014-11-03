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
                "EXAMPLE: " + Colors.BOLD + "ALIAS Welcome SAY Welcome $1 to $CHANNEL." + Colors.NORMAL + " Will generate the alias \"" + Colors.BOLD + "welcome" + Colors.NORMAL + "\" that will greet anything passed as the first parameter",
        };
    }

    @Override
    public void run(User user, Channel channel, String[] params) {
        if (params[0].equalsIgnoreCase("-rm")) {
            String alias = params[1].toUpperCase();
            if (Settings.aliases.containsKey(alias)) {
                //Build new aliases setting
                String newAliases ="";
                for (String s: Settings.aliases.keySet()) {
                    if (s.equalsIgnoreCase(alias)) continue;
                    newAliases += (newAliases.equals("") ? "" : ",") + s + "," + Settings.aliases.get(s).replaceAll("\"", "\\\\\"");
                }

                Main.getConfig().changeSetting("aliases", newAliases);

                Main.getBot().reply(channel, user, "Removed alias " + Colors.BLUE + alias);

                return;
            } else {
                Main.getBot().reply(channel, user, "Could not remove alias " + Colors.RED + params[1] + Colors.NORMAL + ": Not found.");
            }
        }
        if (!CommandHandler.isCommand(params[1])) {
            Main.getBot().reply(channel, user, "Could not make alias " + Colors.RED + params[0] + Colors.NORMAL + " as " + Colors.BOLD + params[1] + Colors.NORMAL + " was not recognized as a valid command or alias");
            return;
        }
        if (!CommandHandler.isCommand(params[0])) {
            String name = params[0].toUpperCase();
            String command = StringUtils.join(params, " ").substring(name.length() + 1);
            Main.getConfig().appendSetting("aliases", ",", name + ",\"" + command.replaceAll("\"", "\\\\\"") + "\"");
            Main.getBot().reply(channel, user, "Successfully bound alias " + Colors.BOLD + Colors.BLUE + name + Colors.NORMAL + " to " + Colors.BOLD + Colors.GREEN + command);
        } else {
            Main.getBot().reply(channel, user, "Could not bind " + Colors.BOLD + Colors.RED + params[0] + Colors.NORMAL + " as it is already in use.");
        }
    }
}

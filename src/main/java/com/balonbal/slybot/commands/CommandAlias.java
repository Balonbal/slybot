package com.balonbal.slybot.commands;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.core.CommandHandler;
import com.balonbal.slybot.lib.Reference;
import com.balonbal.slybot.lib.Settings;
import org.apache.commons.lang3.StringUtils;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.Event;

public class CommandAlias implements Command {
    @Override
    public String getTrigger() {
        return "alias";
    }

    @Override
    public int requiresOP() {
        return Reference.REQUIRES_OP_BOT;
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
    public void run(String[] parameters, Event<SlyBot> event) {
        if (parameters[1].equalsIgnoreCase("-rm")) {
            String alias = parameters[2].toUpperCase();
            if (Settings.aliases.containsKey(alias)) {
                //Build new aliases setting
                String newAliases ="";
                for (String s: Settings.aliases.keySet()) {
                    if (s.equalsIgnoreCase(alias)) continue;
                    newAliases += (newAliases.equals("") ? "" : ",") + s + ",\"" + Settings.aliases.get(s).replaceAll("\"", "\\\\\"") + "\"";
                }

                Main.getConfig().changeSetting("aliases", newAliases);

                event.getBot().reply(event, "Removed alias " + Colors.BLUE + alias);
            } else {
                event.getBot().reply(event, "Could not remove alias " + Colors.RED + parameters[2] + Colors.NORMAL + ": Not found.");
            }

            return;
        } else if (parameters[1].equalsIgnoreCase("--list") || parameters[1].equalsIgnoreCase("-l")) {
            if (parameters.length > 2) {
                if (Settings.aliases.containsKey(parameters[2].toUpperCase())) {
                    event.getBot().reply(event, Settings.aliases.get(parameters[2].toUpperCase()));
                } else {
                    event.getBot().reply(event, "No such alias.");
                }
            } else {
                event.getBot().reply(event, "Loaded aliases: " + StringUtils.join(Settings.aliases.keySet(), ", "));
            }
            return;
        }
        if (!Main.getCommandListener().getCommandHandler().isCommand(parameters[2])) {
            event.getBot().reply(event, "Could not make alias " + Colors.RED + parameters[1] + Colors.NORMAL + " as " + Colors.BOLD + parameters[2] + Colors.NORMAL + " was not recognized as a valid command or alias");
            return;
        }
        if (!Main.getCommandListener().getCommandHandler().isCommand(parameters[1])) {
            String name = parameters[1].toUpperCase();
            String command = StringUtils.join(parameters, " ").substring(parameters[0].length() + name.length() + 2);
            Main.getConfig().appendSetting("aliases", ",", name + ",\"" + command.replaceAll("\"", "\\\\\"") + "\"");
            event.getBot().reply(event, "Successfully bound alias " + Colors.BOLD + Colors.BLUE + name + Colors.NORMAL + " to " + Colors.BOLD + Colors.GREEN + command);
        } else {
            event.getBot().reply(event, "Could not bind " + Colors.BOLD + Colors.RED + parameters[1] + Colors.NORMAL + " as it is already in use.");
        }
    }
}

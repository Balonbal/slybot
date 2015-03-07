package com.balonbal.slybot.commands;

import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.lib.Reference;
import com.balonbal.slybot.lib.Settings;
import com.balonbal.slybot.util.sites.mal.MyAnimeList;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;

import java.util.HashMap;

public class CommandAccounts implements Command {
    @Override
    public String getTrigger() {
        return "ACCOUNTS";
    }

    @Override
    public int requiresOP() {
        return Reference.REQUIRES_OP_BOT;
    }

    @Override
    public boolean channelCommand() {
        return false;
    }

    @Override
    public boolean pmCommand() {
        return true;
    }

    @Override
    public String[] help() {
        return new String[] {
                "Used to manage accounts for various services. SYNTAX: " + Colors.BOLD + "ACCOUNTS <add|remove> <service> [username] [password]",
                "Currently supported services: MAL (http://myanimelist.net)"
        };
    }

    @Override
    public String run(String[] parameters, Event<SlyBot> event) {
        if (parameters.length == 3 && parameters[1].equalsIgnoreCase("remove")) {
            if (Settings.accounts.containsKey(parameters[2].toLowerCase())) {
                Settings.accounts.remove(parameters[2].toLowerCase());
                event.getBot().reply(event, "Successfully removed account.");
            } else {
                event.getBot().reply(event, "That service does not have an associated account.");
            }
        } else if (parameters.length == 5 && parameters[1].equalsIgnoreCase("add")) {
            String service = parameters[2].toLowerCase();
            String username = parameters[3];
            String password = parameters[4];

            if (Settings.accounts.containsKey(service)) {
                event.getBot().reply(event, "Error, service already tied to an account.");
                return "";
            }

            if (service.equalsIgnoreCase("mal")) {
                //Verify that the credentials are valid
                if (!MyAnimeList.checkLogin(username, password)) {
                    event.getBot().reply(event, "Error, account information was invalid.");
                    return "";
                }

                HashMap<String, String> usermap = new HashMap<>();

                usermap.put("username", username);
                usermap.put("password", password);

                Settings.accounts.put(service, usermap);

                event.getBot().reply(event, "Successfully associated account with service.");
            } else {
                event.getBot().reply(event, "Error, unrecognized service.");
            }
        } else {
            event.getBot().reply(event, "Insufficient arguments, SYNTAX: \" + Colors.BOLD + \"ACCOUNTS <add|remove> <service> [username] [password]");
        }
        return "";
    }
}

package com.balonbal.slybot.commands;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.lib.Reference;
import com.balonbal.slybot.lib.Settings;
import org.apache.commons.lang3.StringUtils;
import org.pircbotx.Colors;
import org.pircbotx.User;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class CommandConfig implements Command {
    @Override
    public String getTrigger() {
        return "CONFIG";
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
                "Used to set, unset and reset configuration values",
                "SYNTAX: " + Colors.BOLD + getTrigger() + " <set|load|save> [key] [value]",
                "Set with no key will return a list of keys + values",
                "Can only be used over PM"
        };
    }

    @Override
    public String run(String[] parameters, Event<SlyBot> event) {
        if (parameters.length < 2) return "false";
        if (!(event instanceof PrivateMessageEvent)) return "false";

        User u = ((PrivateMessageEvent) event).getUser();

        String command = parameters[1];
        HashMap<String, Object> map = event.getBot().getConfig().getSaveValues();

        if (command.equalsIgnoreCase("set")) {
            if (parameters.length < 3) {
                for (String key: map.keySet()) {
                    String value = "";
                    if (key.equalsIgnoreCase(Reference.CONFIG_ALIASES)) {
                        continue; //No need to print this
                    } else if (key.equalsIgnoreCase(Reference.CONFIG_BOTPASS) || key.equalsIgnoreCase(Reference.CONFIG_NICKPASS) && map.containsKey(key)) {
                        value = (map.get(key).equals("")) ? "Not set" : "Set"; //Do not send passwords
                    }else if (map.containsKey(key) && map.get(key) instanceof ArrayList) {
                        ArrayList<String> list = (ArrayList<String>) map.get(key);

                        value = StringUtils.join(list.toArray(new String[list.size()]), ", ");
                    } else {
                        value = map.containsKey(key) ? String.valueOf(map.get(key)) : "Not recognized";
                    }
                    event.getBot().reply(event, String.format(" - %s: %s", key, (value.equals("") ? "Not set" : value)));
                }
                return "true";
            } else if (parameters.length > 3) {
                //Verify that it is a valid key
                String key = parameters[2];
                if (map.containsKey(key)) {
                    //Get the value to be set
                    String[] value = new String[parameters.length - 3];
                    System.arraycopy(parameters, 3, value, 0, value.length);

                    //Get the current value from the map
                    Object currValue = map.get(key);
                    Object newValue = "";

                    //Aliases can not be changed this way
                    if (key.equalsIgnoreCase(Reference.CONFIG_ALIASES)) return "false";
                    //Only owners can change ownership
                    if ((key.equalsIgnoreCase(Reference.CONFIG_OWNER) || key.equalsIgnoreCase(Reference.CONFIG_BOTPASS)) && (!Settings.owner.equalsIgnoreCase(u.getNick()) || !u.isVerified())) return "false";
                    if (currValue instanceof ArrayList) {
                        ArrayList<String> list = new ArrayList<String>();
                        Collections.addAll(list, value);
                        newValue = list;
                    } else {
                        newValue = StringUtils.join(value, " ");
                    }

                    event.getBot().getConfig().updateSetting(key, newValue);
                    //Change the bot's nickname
                    if (key.equals(Reference.CONFIG_BOTNICK)) event.getBot().sendRaw().rawLine("nick " + newValue);
                    event.getBot().reply(event, "Successfully updated key: " + key);
                    return "true";
                } else {
                    event.getBot().reply(event, "No such key: " + key);
                }
            } else {
                event.getBot().reply(event, "Invalid number of arguments, try again");
            }
        } else if (command.equalsIgnoreCase("load")) {
            try {
                Main.getConfig().loadFromFile(Reference.CONFIG_BOTCONFIG_ID);
                event.getBot().reply(event, "Successfully reloaded bot configuration from file.");
                return "true";
            } catch (FileNotFoundException e) {
                event.getBot().reply(event, "Could not load config: File not found.");
            }
        } else if (command.equalsIgnoreCase("save")) {
            Main.getConfig().save(Reference.CONFIG_BOTCONFIG_ID);
            event.getBot().reply(event, "Successfully saved bot configuration to file.");
            return "true";
        }
        return "false";
    }
}

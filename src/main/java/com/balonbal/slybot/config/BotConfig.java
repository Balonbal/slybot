package com.balonbal.slybot.config;

import com.balonbal.slybot.core.SlyConfiguration;
import com.balonbal.slybot.lib.Settings;

import java.util.HashMap;

public class BotConfig extends SlyConfiguration {
	
	public BotConfig() {
		super("configuration.config");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateSettings() {
		//Set all the settings TODO Improve this
		Settings.owner = configuration.get("owner");
		Settings.botops = (configuration.get("botops").contains(",") ? configuration.get("botops").split(",") : new String[] { configuration.get("botops") });
		Settings.botnick = configuration.get("botnick");
		Settings.nickpass = configuration.get("nickpass");
		Settings.network = configuration.get("default_network");
		Settings.channels = (configuration.get("default_channels").contains(",") ? configuration.get("default_channels").split(",") : new String[] { configuration.get("default_channels") });
		Settings.operatorpass = configuration.get("operatorpass");
        Settings.loggedChannels = (configuration.get("loggedChannels").contains(",") ? configuration.get("loggedChannels").split(",") : new String[]{configuration.get("loggedChannels")});
        Settings.mutedChannels = (configuration.get("mutedChannels").contains(",") ? configuration.get("mutedChannels").split(",") : new String[]{configuration.get("mutedChannels")});
        String alias = configuration.get("aliases");
        HashMap<String, String> aliases = new HashMap<String, String>();
        while (alias.contains(",")) {
            int nameStart = 0;

            //If there are multiple aliases, they start with a ","
            if (alias.startsWith(",")) {
                nameStart = 1;
            }
            String name = alias.substring(nameStart, alias.indexOf(",", nameStart+1));

            int startPos = alias.indexOf("\"")+1;
            int endPos = alias.indexOf("\"", startPos+1);
            //Escape any escaped quotes
            while (alias.charAt(endPos-1) == '\\') {
                endPos = alias.indexOf("\"", endPos+1);
            }

            //Fetch the command and update the string
            String command = alias.substring(startPos, endPos);
            alias = alias.substring(endPos+1);

            //Update the alias list
            aliases.put(name, command.replaceAll("\\\\\"", "\""));
        }
        Settings.aliases = aliases;

    }

	@Override
	public void createSettings() {
		addSetting("owner", "");
		addSetting("botops", "");
		addSetting("botnick", "");
		addSetting("nickpass", "");
		addSetting("default_network", "");
		addSetting("default_channels", "");
		addSetting("operatorpass", "");
        addSetting("loggedChannels", "");
        addSetting("mutedChannels", "");
        addSetting("aliases", "");
    }

}

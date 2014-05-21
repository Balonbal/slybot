package slybot.core;

import slybot.lib.Settings;

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
	}

}

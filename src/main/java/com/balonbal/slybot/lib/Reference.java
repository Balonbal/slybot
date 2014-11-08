package com.balonbal.slybot.lib;

import com.balonbal.slybot.Main;

public class Reference {
	
	public static final String BOTNICK = Main.getBot().getNick();
	public static final String[] PREFIXES = {
		BOTNICK + ": ",
		BOTNICK + " ",
		BOTNICK + ", ",
		"."
	};
	
	public static final int ACCEPT_TIMEOUT = 60;
	public static final int TTT_NEXT_TURN_TIMEOUT = 180;

    /* PERMISSION CONSTANTS */
    public static final int REQUIRES_OP_NONE = -1;
    public static final int REQUIRES_OP_ANY = 0;
    public static final int REQUIRES_OP_CHANNEL = 1;
    public static final int REQUIRES_OP_BOT = 2;
    public static final int REQUIRES_OP_BOTH = 3;

    /* CONFIGURATION CONSTANTS */
    public static final String CONFIG_ALIASES = "aliases";
    public static final String CONFIG_AUTOSAVE = "autosaveFrequency";
    public static final String CONFIG_BOTNICK = "botnick";
    public static final String CONFIG_BOTOPS = "botops";
    public static final String CONFIG_BOTPASS = "operatorPass";
    public static final String CONFIG_CHANNELS = "channels";
    public static final String CONFIG_MUTED_CHANNELS = "mutedChannels";
    public static final String CONFIG_NETWORK = "network";
    public static final String CONFIG_NICKPASS = "nickpass";
    public static final String CONFIG_OWNER = "owner";

    public static final String CONFIG_BOTCONFIG_ID = "botConfig";
}

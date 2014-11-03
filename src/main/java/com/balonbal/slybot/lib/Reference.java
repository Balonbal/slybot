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

}

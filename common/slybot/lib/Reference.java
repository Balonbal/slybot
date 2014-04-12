package slybot.lib;

import slybot.Main;

public class Reference {
	
	public static final String[] PREFIXES = {
		Main.getNick() + ": ",
		Main.getNick() + " ",
		"."
	};
	
	public static final String VERSION = "v1.0.2";

	public static final int ACCEPT_TIMEOUT = 60;
	public static final int TTT_NEXT_TURN_TIMEOUT = 60*3;

}

package slybot;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;

public class SlyBot extends PircBotX {

	public SlyBot(Configuration<? extends PircBotX> configuration) {
		super(configuration);
	}
	
	public void quit() {
		this.sendRaw().rawLine("part");
	}

}

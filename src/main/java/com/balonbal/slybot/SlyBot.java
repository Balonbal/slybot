package com.balonbal.slybot;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;

import java.util.logging.Logger;

public class SlyBot extends PircBotX {

    private static final Logger logger = Logger.getLogger(SlyBot.class.getName());

	public SlyBot(Configuration<? extends PircBotX> configuration) {
		super(configuration);
	}
	
	public void quit() {
		this.sendRaw().rawLine("part");
	}
	
	public void shutdown() {
        logger.info("Got instructions to shut down.. Shutting down.");
        this.stopBotReconnect();
		this.sendRaw().rawLine("quit");
	}

}

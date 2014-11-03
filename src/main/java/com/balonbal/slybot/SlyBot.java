package com.balonbal.slybot;

import com.balonbal.slybot.lib.Settings;
import org.pircbotx.Channel;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

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
        System.out.println("Got instructions to shut down.. Shutting down.");
        this.stopBotReconnect();
		this.sendRaw().rawLine("quit");
	}

    public void reply(Channel c, User u, String message) {
        if (c == null) {
            u.send().message(message);
        } else {
            for (String s: Settings.mutedChannels) {
                if (s.equalsIgnoreCase(c.getName())) return;
            }
            c.send().message(message);
        }
    }

}

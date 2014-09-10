package com.balonbal.slybot.listeners;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.commands.Command;
import com.balonbal.slybot.core.CommandHandler;
import com.balonbal.slybot.lib.Reference;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.Listener;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Logger;

public class CommandListener implements Listener<PircBotX> {

    private static final Logger logger = Logger.getLogger(CommandListener.class.getName());

    private ArrayList<Command> commands;

    public CommandListener() {
        addCommands();
	}

	private void addCommands() {
		commands = new ArrayList<Command>();
		Reflections r = new Reflections("");
		//Get the subtypes of the command class
		Set<Class<? extends Command>> classes = r.getSubTypesOf(Command.class);
		
		//loop through found classes and add them as commands
		for (Class<? extends Command> c: classes) {
			try {
				Command cmd = c.newInstance();
                System.out.println("Loaded command: " + c.getName());
                getCommands().add(cmd);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onEvent(Event<PircBotX> arg0) throws Exception {
		//Check for channel messages
		if (arg0 instanceof MessageEvent) {
			MessageEvent<PircBotX> e = (MessageEvent<PircBotX>) arg0;
			
			String message = e.getMessage();

            final Logger channelLog = Logger.getLogger(e.getChannel().getName());
            channelLog.info(String.format("%s -> %s: %s", e.getUser().getNick(), e.getChannel().getName(), message));
            //check if the message is a bot command
			for (String s: Reference.PREFIXES) {
				if (message.toUpperCase().startsWith(s.toUpperCase())) {
					message = message.substring(s.length());
					//Run command
					CommandHandler.processCommand(e.getUser(), e.getChannel(), message);
				}
			}
			
			//try next turn TODO consider if this needs a command
			Main.getChallengeManager().doNextTurn(e.getUser(), message.split(" "));
		//check for PMs
		} else if (arg0 instanceof PrivateMessageEvent) {
			PrivateMessageEvent<PircBotX> e = (PrivateMessageEvent<PircBotX>) arg0;
			
			CommandHandler.processCommand(e.getUser(), null, e.getMessage());
		}
	}

	public ArrayList<Command> getCommands() {
		return commands;
	}

}

package slybot;

import java.util.ArrayList;
import java.util.Set;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.Listener;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.reflections.Reflections;

import slybot.commands.Command;
import slybot.core.CommandHandler;
import slybot.lib.Reference;
import slybot.lib.Strings;
import slybot.tools.Youtube;

public class SlyListener implements Listener<PircBotX> {
	
	private ArrayList<Command> commands;
	
	public SlyListener() {
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
				System.out.println("Found command: " + c.getName());
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
			
			//check if the message is a bot command
			for (String s: Reference.PREFIXES) {
				if (message.toUpperCase().startsWith(s.toUpperCase())) {
					message = message.substring(s.length());
					//Run command
					CommandHandler.processCommand(e.getUser(), e.getChannel(), message, isOP(e));
				}
			}
			
			//try next turn TODO consider if this needs a command
			Main.getChallengeManager().doNextTurn(e.getUser(), message.split(" "));
			
			for (String s: Strings.youtubes) {
				if (message.toLowerCase().contains(s.toLowerCase())) {
					//Extract the youtube link
					Youtube y = new Youtube(message.substring(message.toLowerCase().indexOf(s)));
					//Print the information and delete the file
					y.printInfo(e.getChannel());
					y.deleteFile();
				}
			}
		//check for PMs
		} else if (arg0 instanceof PrivateMessageEvent) {
			PrivateMessageEvent<PircBotX> e = (PrivateMessageEvent<PircBotX>) arg0;
			
			CommandHandler.processCommand(e.getUser(), null, e.getMessage(), false);
		}
	}
	
	private boolean isOP(MessageEvent<?> e) {
		return e.getChannel().getOps().contains(e.getUser());
	}

	public ArrayList<Command> getCommands() {
		return commands;
	}


}

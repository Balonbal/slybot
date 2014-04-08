package slybot;

import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.Listener;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import slybot.commands.CommandHandler;
import slybot.lib.Reference;

public class SlyListener implements Listener {

	@Override
	public void onEvent(Event arg0) throws Exception {
		//Check for channel messages
		if (arg0 instanceof MessageEvent) {
			MessageEvent e = (MessageEvent) arg0;
			
			String message = e.getMessage();
			
			//check if the message is a bot command
			for (String s: Reference.PREFIXES) {
				if (message.toUpperCase().startsWith(s.toUpperCase())) {
					message = message.substring(s.length());
					CommandHandler.processCommand((SlyBot) e.getBot(), e.getUser(), e.getChannel(), message, isOP(e));
				}
			}
		//check for PMs
		} else if (arg0 instanceof PrivateMessageEvent) {
			PrivateMessageEvent e = (PrivateMessageEvent) arg0;
			
			CommandHandler.processCommand((SlyBot) e.getBot(), e.getUser(), null, e.getMessage(), false);
		}
	}
	
	private boolean isOP(MessageEvent e) {
		return e.getChannel().getOps().contains(e.getUser());
	}
	

}

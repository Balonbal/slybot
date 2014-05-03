package slybot;

import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.Listener;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import slybot.core.CommandHandler;
import slybot.lib.Reference;
import slybot.lib.Strings;
import slybot.tools.Youtube;

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
					//Run command
					CommandHandler.processCommand((SlyBot) e.getBot(), e.getUser(), e.getChannel(), message, isOP(e));
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
			PrivateMessageEvent e = (PrivateMessageEvent) arg0;
			
			CommandHandler.processCommand((SlyBot) e.getBot(), e.getUser(), null, e.getMessage(), false);
		}
	}
	
	private boolean isOP(MessageEvent e) {
		return e.getChannel().getOps().contains(e.getUser());
	}
	

}

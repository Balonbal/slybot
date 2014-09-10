package com.balonbal.slybot.listeners;

import com.balonbal.slybot.lib.Strings;
import com.balonbal.slybot.util.Youtube;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.Listener;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.logging.Logger;

public class LinkListener implements Listener<PircBotX> {

    public static final Logger logger = Logger.getLogger(LinkListener.class.getName());

    @Override
    public void onEvent(Event<PircBotX> pircBotXEvent) throws Exception {
        if (pircBotXEvent instanceof MessageEvent) {

            MessageEvent<PircBotX> messageEvent = (MessageEvent<PircBotX>) pircBotXEvent;
            //Extract the message
            String message = messageEvent.getMessage();

            for (String s : Strings.youtubes) {
                if (message.toLowerCase().contains(s.toLowerCase())) {
                    System.out.println(String.format("Found matching link for domain %s in message from user %s, starting search", s, messageEvent.getUser().getNick()));
                    //Extract the youtube link
                    Youtube y = new Youtube(message.substring(message.toLowerCase().indexOf(s)));
                    //Print the information and delete the file
                    y.printInfo(messageEvent.getChannel());
                    y.deleteFile();
                }
            }
        }
    }
}

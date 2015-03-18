package com.balonbal.slybot.listeners;

import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.lib.Strings;
import com.balonbal.slybot.util.sites.Youtube;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import java.util.regex.Matcher;

public class LinkListener extends ListenerAdapter<SlyBot> {

    private Youtube youtube = new Youtube();

    @Override
    public void onMessage(MessageEvent<SlyBot> pircBotXEvent) {
        //Extract the message
        String message = pircBotXEvent.getMessage();

        Matcher youtubes = Strings.youtube.matcher(message);

        while (youtubes.find()) {
            //Split start of url to
            String url = message.substring(youtubes.end());
            if (url.contains(" ")) url = url.substring(0, url.indexOf(" "));

            try {
                youtube.parseVideoInfo(url, pircBotXEvent);
            } catch (IllegalArgumentException e) {
                url = message.substring(youtubes.start());
                if (url.contains(" ")) url = url.substring(0, url.indexOf(" "));
                pircBotXEvent.getBot().reply(pircBotXEvent, "Sorry, your youtube URL (" + url + ") gave me no results");
            }
        }
    }

    public void onPrivateMessage(PrivateMessageEvent<SlyBot> pircBotXEvent) {
        //Extract the message
        String message = pircBotXEvent.getMessage();

        Matcher youtubes = Strings.youtube.matcher(message);

        while (youtubes.find()) {
            //Split start of url to
            String url = message.substring(youtubes.end(), message.indexOf(" ", youtubes.start()));

            try {
                youtube.parseVideoInfo(url, pircBotXEvent);
            } catch (IllegalArgumentException e) {
                pircBotXEvent.getBot().reply(pircBotXEvent, "Sorry, your youtube URL gave me no results");
            }
        }
    }

}

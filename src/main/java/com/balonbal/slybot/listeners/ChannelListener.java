package com.balonbal.slybot.listeners;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.config.ChannelConfig;
import com.balonbal.slybot.lib.Reference;
import com.balonbal.slybot.lib.Settings;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.PartEvent;

import java.io.File;

public class ChannelListener extends ListenerAdapter<SlyBot> {

    @Override
    public void onJoin(JoinEvent<SlyBot> event) throws Exception {
        //When the bot enters a channel
        if (event.getUser().equals(event.getBot().getUserBot())) {
            System.out.println("Entered channel: " + event.getChannel().getName());

            //Find save path for config file
            String location = Reference.CONFIG_CHANNEL_FILE;
            location = location.replaceAll("\\$NETWORK", Settings.network);
            location = location.replaceAll("\\$CHANNEL", event.getChannel().getName());

            Main.getConfig().addConfiguration(new File(location), "config" + event.getChannel().getName(), new ChannelConfig(event.getChannel().getName()));
        }
    }

    @Override
    public void onPart(PartEvent<SlyBot> event) throws Exception {
        //When the bot leaves a channel
        if (event.getUser().equals(event.getBot().getUserBot())) {
            System.out.println("Left channel: " + event.getChannel().getName());
        }
    }
}

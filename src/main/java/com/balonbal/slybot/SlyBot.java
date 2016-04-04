package com.balonbal.slybot;

import com.balonbal.slybot.config.BotConfig;
import com.balonbal.slybot.config.ChannelConfig;
import com.balonbal.slybot.config.Config;
import com.balonbal.slybot.lib.Settings;
import com.balonbal.slybot.util.LoggerUtil;
import com.balonbal.slybot.util.rss.RSSManager;
import com.balonbal.slybot.util.sites.twitch.Twitch;
import org.pircbotx.Channel;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import java.util.Date;

public class SlyBot extends PircBotX {

    protected Config config;
    protected String version = Main.class.getPackage().getImplementationVersion();
    protected RSSManager rssManager;
    protected Twitch twitch;

	public SlyBot(Configuration<? extends PircBotX> configuration, BotConfig botConfig) {
		super(configuration);
        config = botConfig;
        if (version == null) version = "development build";
	}

    public void onConnected() {
        try {
            rssManager = new RSSManager();
            twitch = new Twitch();

            Main.getConfig().addConfiguration(twitch.getSaveLocation(), "twitch", twitch);
            Main.getConfig().addConfiguration(rssManager.getSaveLocation(), "rss", rssManager);
            Main.getConfig().addConfiguration(Main.getStatsCacher().getSaveLocation(), "stats", Main.getStatsCacher());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public void quit() {
		this.sendRaw().rawLine("part");
	}
	
	public void shutdown() {
        System.out.println("Got instructions to shut down.. Shutting down.");
        Main.getConfig().saveAll();
        Main.getConfig().stopAutoSave();
        this.stopBotReconnect();
        this.shutdown(true);
		//this.sendRaw().rawLine("quit");
	}

    public void reply(Channel c, User u, String message) {
        if (Settings.suppressOutput) return;
        if (c == null) {
            u.send().message(message);
        } else {
            for (String s : Settings.mutedChannels) {
                if (s.equalsIgnoreCase(c.getName())) return;
            }
            c.send().message(message);
            LoggerUtil.log(Settings.network, c.getName().toLowerCase(), new Date(), getUserBot(), message);
        }
    }

    public void reply (Event e, String message) {
        if (e instanceof MessageEvent) {
            reply(((MessageEvent) e).getChannel(), ((MessageEvent) e).getUser(), message);
        } else if (e instanceof PrivateMessageEvent) {
            e.respond(message);
        }
    }

    public void replyLots(Event e, String ... message) {
        for (String s: message) {
            //Send lines as separate messages
            if (s.contains("\n")) {
                replyLots(e, s.split("\\n"));
            } else {
                reply(e, s);
            }
        }
    }

    public void sendMessage(String destination, String message) {
        //Channels
        if (destination.startsWith("#")) {
            Channel channel = getUserChannelDao().getChannel(destination);
            reply(channel, null, message);
        //Users
        } else {
            User user = getUserChannelDao().getUser(destination);
            reply(null, user, message);
        }
    }


    public boolean isBotOP(User user) {
        //Only include users verified by the nickserv
        if (user.isVerified()) {
            for (String s: Settings.botops) {
                if (s.equalsIgnoreCase(user.getNick())) {
                    return true;
                }
            }

        }
        return false;
    }

    public Config getConfig() {
        return config;
    }

    public String getVersion() {
        return  version;
    }

    public RSSManager getRssManager() {
        return rssManager;
    }

    public void updateNick(String newValue) {
        //Update nick
        this.sendRaw().rawLine("nick " + newValue);
        this.setNick(newValue);

        //Update config in all connected channels
        System.out.println("Rebuilding triggers for " + this.getUserChannelDao().getAllChannels().size() + " channels.");
        for (Channel c: this.getUserChannelDao().getAllChannels()) {
            ChannelConfig channelConfig = (ChannelConfig) Main.getConfig().getConfig("config" + c.getName());

            //Rebuild the triggger
            channelConfig.setTrigger(channelConfig.getTriggerString());
        }
    }

    public Twitch getTwitch() {
        return twitch;
    }
}

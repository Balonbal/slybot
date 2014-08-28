package com.balonbal.slybot.config;

import com.balonbal.slybot.core.SlyConfiguration;
import com.balonbal.slybot.lib.Strings;
import org.pircbotx.Channel;

import java.util.HashMap;

public class ChannelConfig extends SlyConfiguration{

    private Channel channel;
    String[] blackListedCommands;
    HashMap<String, String> aliasCommands;

    public ChannelConfig(Channel channel) {
        super(channel.getName());
        this.channel = channel;
        blackListedCommands = new String[] { };

    }

    @Override
    public void updateSettings() {

    }

    @Override
    public void createSettings() {
        addSetting(Strings.CHANNEL_SETTING_BLACKLISTED_COMMANDS, "");
        addSetting(Strings.CHANNEL_SETTING_ALIASES, "");
    }
}

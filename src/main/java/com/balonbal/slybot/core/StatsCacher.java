package com.balonbal.slybot.core;

import com.balonbal.slybot.util.stats.ChannelStats;

import java.util.HashMap;

public class StatsCacher {
    HashMap<String, ChannelStats> channels;

    public StatsCacher() {
        channels = new HashMap<>();
    }

    public void buildCache(String channel) {
        System.out.println("Building channel cache.. might take a while for long logs");
        ChannelStats stats = new ChannelStats(channel);
        channels.put(channel, stats);
    }

    public ChannelStats getStats(String channel) {
        return channels.get(channel);
    }

}

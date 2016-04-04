package com.balonbal.slybot.core;

import com.balonbal.slybot.util.stats.ChannelStats;

public class StatsCacher {

    public void buildCache(String channel) {
        System.out.println("Building channel cache.. might take a while for long logs");
        ChannelStats stats = new ChannelStats(channel);
    }

}

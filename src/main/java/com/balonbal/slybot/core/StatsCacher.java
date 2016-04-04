package com.balonbal.slybot.core;

import com.balonbal.slybot.config.Config;
import com.balonbal.slybot.lib.Reference;
import com.balonbal.slybot.util.stats.ChannelStats;

import java.io.File;
import java.util.HashMap;

public class StatsCacher implements Config {
    HashMap<String, ChannelStats> channels;
    public long lastOnlineTick;
    public long currOnline = 0, onlineTime = 0, offlineTime = 0;

    public StatsCacher() {
        channels = new HashMap<>();
        lastOnlineTick = System.currentTimeMillis();
    }

    public void buildCache(String channel) {
        System.out.println("Building channel cache.. might take a while for long logs");
        ChannelStats stats = new ChannelStats(channel);
        channels.put(channel, stats);
    }

    public ChannelStats getStats(String channel) {
        return channels.get(channel);
    }

    @Override
    public void updateSetting(String key, Object value) {
        switch (key) {
            case "offlineTime":
                offlineTime += Long.parseLong(String.valueOf(value));
                break;
            case "onlineTime":
                onlineTime += Long.parseLong(String.valueOf(value));
                break;
            case "lastOnlineTick":
                offlineTime += System.currentTimeMillis() - Long.parseLong(String.valueOf(value)); //Add offline time
                lastOnlineTick = System.currentTimeMillis();
                break;
        }
        System.out.println(offlineTime);
    }

    @Override
    public void appendSetting(String key, Object value) {
        //Nope
    }

    @Override
    public void removeSetting(String key, Object value) {
        //Nope
    }

    @Override
    public HashMap<String, Object> getSaveValues() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("offlineTime", offlineTime);
        currOnline += System.currentTimeMillis() - lastOnlineTick;
        onlineTime += System.currentTimeMillis() - lastOnlineTick;
        map.put("onlineTime", onlineTime);
        lastOnlineTick = System.currentTimeMillis();
        map.put("lastOnlineTick", lastOnlineTick);
        return map;
    }

    @Override
    public void setSaveLocation(File file) {
        //Nope
    }

    @Override
    public File getSaveLocation() {
        return new File(Reference.CONFIG_BASE_DIR + "stats.yaml");
    }
}

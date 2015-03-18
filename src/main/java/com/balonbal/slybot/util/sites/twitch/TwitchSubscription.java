package com.balonbal.slybot.util.sites.twitch;

import java.util.ArrayList;
import java.util.HashMap;

public class TwitchSubscription {

    boolean active;
    String name;
    ArrayList<String> subscribers;
    String channel;

    protected HashMap<String, Object> streamData;

    public TwitchSubscription(String name, String channel, String ... subscribers) {
        this.name = name;
        this.channel = channel;
        active = false;

        this.subscribers = new ArrayList<>();

        for (String sub: subscribers) {
            this.subscribers.add(sub);
        }
    }

    public TwitchSubscription(String name, String channel, ArrayList<String> subscribers) {
        this.name = name;
        this.channel = channel;
        this.subscribers = subscribers;
    }

    public HashMap<String, Object> createMap() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("name", name);
        map.put("subscribers", subscribers);
        map.put("channel", channel);

        return map;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public ArrayList<String> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(ArrayList<String> subscribers) {
        this.subscribers = subscribers;
    }

    public HashMap<String, Object> getStreamData() {
        return streamData;
    }

    public void setStreamData(HashMap<String, Object> streamData) {
        this.streamData = streamData;
    }

    public boolean isSubscribed(String name) {
        return subscribers.contains(name);
    }

    public void unsubscribe(String name) {
        subscribers.remove(name);
    }

    public void subscribe(String subscriber) {
        subscribers.add(subscriber);
    }
}

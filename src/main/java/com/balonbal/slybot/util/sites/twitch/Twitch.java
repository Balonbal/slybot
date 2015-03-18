package com.balonbal.slybot.util.sites.twitch;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.config.Config;
import com.balonbal.slybot.lib.Reference;
import com.balonbal.slybot.lib.Settings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import org.pircbotx.Colors;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Twitch implements Config {

    private ArrayList<TwitchSubscription> subscriptions;
    private File file;

    public Twitch() {
        subscriptions = new ArrayList<>();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                notifySubscribers();
            }
        }, Settings.twitchUpdateFrequency, Settings.twitchUpdateFrequency);

        file = new File("TwitchSubscriptions.yaml");
    }

    public ArrayList<HashMap<String, Object>> checkSubscriptions() {
        //Build a single request (so we only have to send one)
        String request = Reference.TWITCH_STREAM_QUERY;

        for (int i = 0; i < subscriptions.size(); i++) {
            //Add a , between each stream
            request += subscriptions.get(i).getChannel() + (i == subscriptions.size() - 1 ? "" : ",");
        }

        try {
            URL requestURL = new URL(request);
            InputStreamReader inputStreamReader = new InputStreamReader(requestURL.openStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            Gson gson = new GsonBuilder().create();
            TwitchStreams streams = gson.fromJson(bufferedReader, TwitchStreams.class);

            return streams.streams;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public void notifySubscribers() {
        ArrayList<String> active = new ArrayList<>();

        for (HashMap<String, Object> map: checkSubscriptions()) {
            if (!(map.get("channel")  instanceof LinkedTreeMap)) {
                System.out.println("ERROR While fetching stream data, this should be reported.");
                continue;
            }
            LinkedTreeMap<String, Object> channel = (LinkedTreeMap<String, Object>) map.get("channel");
            for (TwitchSubscription subscription: subscriptions) {
                //The subscription is online
                if (subscription.getChannel().equalsIgnoreCase(channel.get("name").toString())) {
                    //The channel has gone online since last check
                    if (!subscription.isActive()) {
                        for (String dest: subscription.getSubscribers()) {
                            //Notify subscribers
                            Main.getBot().sendMessage(dest, "[" + Colors.BOLD + Colors.PURPLE + "TWITCH" + Colors.NORMAL + "] " +
                                    "User " + Colors.BLUE + channel.get("display_name") + Colors.NORMAL + " Just went live! " +
                                    "[ Playing: " + Colors.GREEN + map.get("game") + Colors.NORMAL + " ] " +
                                    ((boolean) channel.get("mature") ? "[ " + Colors.RED + "MATURE" + Colors.NORMAL + " ] " : "") +
                                    "(" + Colors.OLIVE + channel.get("url") + Colors.NORMAL + ")");
                        }

                        //Set the stream to active so we don't duplicate subscriptions
                        subscription.setActive(true);
                    }

                    active.add(((String) channel.get("name")).toLowerCase());
                }
            }
        }

        //Update offline streams
        for (TwitchSubscription subscription: subscriptions) {
            if (!active.contains(subscription.getChannel().toLowerCase()) ) {
                subscription.setActive(false);
            }
        }
    }

    @Override
    public void updateSetting(String key, Object value) {
        switch (key) {
            case "subscriptions":
                for (HashMap<String, Object> map: (ArrayList<HashMap<String, Object>>) value) {
                    subscriptions.add(new TwitchSubscription((String) map.get("name"), (String) map.get("channel"), (ArrayList<String>) map.get("subscriptions")));
                }
                break;
        }
    }

    @Override
    public void appendSetting(String key, Object value) {
        //Do nothing
    }

    @Override
    public void removeSetting(String key, Object value) {
        //Do nothing
    }

    @Override
    public HashMap<String, Object> getSaveValues() {
        HashMap<String, Object> map = new HashMap<>();

        //Build a map for the subscriptions
        ArrayList<HashMap<String, Object>> feeds = new ArrayList<>();
        for (TwitchSubscription subscription: subscriptions) {
            feeds.add(subscription.createMap());
        }
        map.put("subscriptions", feeds);

        return map;
    }

    @Override
    public void setSaveLocation(File file) {
        this.file = file;
    }

    @Override
    public File getSaveLocation() {
        return file;
    }

    public TwitchSubscription getSubscription(String name) {
        for (TwitchSubscription subscription: subscriptions) {
            if (subscription.getName().equalsIgnoreCase(name)) {
                return subscription;
            }
        }

        return null;
    }

    public void addSubscription(String channel, String name, ArrayList<String> subscribers) {
        subscriptions.add(new TwitchSubscription(name, channel, subscribers));
    }

    public ArrayList<TwitchSubscription> getSubscriptions() {
        return subscriptions;
    }
}

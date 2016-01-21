package com.balonbal.slybot.util.rss;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.config.Config;
import com.balonbal.slybot.lib.Settings;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class RSSManager implements Config {

    public ArrayList<RSSSubscription> feeds;
    public File savelocation = new File("rss.yaml");
    public Timer timer;

    public RSSManager() {
        feeds = new ArrayList<>();
        startTimer(Settings.rssUpdateFrequency, Settings.rssUpdateFrequency);
    }

    public void addFeed(URL url, String id, ArrayList<String> subscribers) {
        RSSSubscription subscription = new RSSSubscription(url, id, subscribers);
        feeds.add(subscription);
    }

    public boolean unsubscribe(String id, String subscriber) {
        RSSSubscription subscription = getFeed(id);

        if (subscription != null) {
            subscription.unsubscribe(subscriber);
            return true;
        }

        return false;
    }

    public boolean removeFeed(String id) {
        for (RSSSubscription subscription: feeds) {
            if (subscription.getID().equalsIgnoreCase(id)) {
                //Delete the file
                Main.getConfig().removeAndDeleteConfiguration(subscription.getID());
                //Remove the feed from updating
                feeds.remove(subscription);
                return true;
            }
        }

        return false;
    }

    public void startTimer(long delay, long repeat) {
        if (timer != null) timer.cancel();
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateFeeds();
            }
        }, delay, repeat);
    }

    public void updateFeeds() {
        for (RSSSubscription feed: feeds) {
            try {
                feed.update();
            } catch (NullPointerException e) {
                //no url
                e.printStackTrace();
            }
        }
    }

    public ArrayList<RSSSubscription> getFeeds() {
        return feeds;
    }

    public RSSSubscription getFeed(String id) {
        for (RSSSubscription subscription: feeds) {
            if (subscription.getID().equalsIgnoreCase(id)) {
                return subscription;
            }
        }
        return null;
    }

    @Override
    public void updateSetting(String key, Object value) {
        switch (key) {
            case "subscriptions":
                ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>) value;
                for (HashMap<String, Object> map: list) {
                    try {
                        feeds.add(new RSSSubscription(new URL((String) map.get("url")), (String) map.get("id"), (ArrayList<String>) map.get("subscribers"), (long) map.get("lastDate"), (long) map.get("lastCheck")));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
                updateFeeds();
        }
    }

    @Override
    public void appendSetting(String key, Object value) {
        switch (key) {
            case "subscriptions":
                HashMap<String, Object> map = (HashMap<String, Object>) value;
                try {
                    feeds.add(new RSSSubscription(new URL((String) map.get("url")), (String) map.get("id"), (ArrayList<String>) map.get("subscriptions")));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
        }
    }

    @Override
    public void removeSetting(String key, Object value) {

    }

    @Override
    public HashMap<String, Object> getSaveValues() {
        HashMap<String, Object> map = new HashMap<>();
        ArrayList<HashMap<String, Object>> feedList = new ArrayList<HashMap<String, Object>>();
        for (RSSSubscription subscription: feeds) {
            feedList.add(subscription.getSaveValues());
        }
        map.put("subscriptions", feedList);
        return map;
    }

    @Override
    public void setSaveLocation(File file) {
        savelocation = file;
    }

    @Override
    public File getSaveLocation() {
        return savelocation;
    }
}

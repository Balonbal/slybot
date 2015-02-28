package com.balonbal.slybot.util.rss;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.lib.Settings;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class RSSManager {

    public ArrayList<RSSSubscription> feeds;
    public String savelocation = "rss/";
    public Timer timer;

    public RSSManager() {
        feeds = new ArrayList<>();
        loadSubscriptions(savelocation);
        startTimer(Settings.rssUpdateFrequency, Settings.rssUpdateFrequency);
    }

    public void loadSubscriptions(String path) {
        File dir = new File(path);

        if (!dir.exists()) {
            dir.mkdir();
            return;
        }

        //load files from the savedirectory
        for (File f: dir.listFiles()) {
            try {
                System.out.println("Loading rss subscription from: " + f.getAbsolutePath());
                RSSSubscription subscription = new RSSSubscription(f.getName());
                //Load any stored config and add to auto-save
                Main.getConfig().addConfiguration(f, f.getName(), subscription);

                feeds.add(subscription);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        updateFeeds();
    }

    public void addFeed(URL url, String id, ArrayList<String> subscribers) {
        RSSSubscription subscription = new RSSSubscription(url, id, subscribers);
        Main.getConfig().addConfiguration(new File(savelocation + id), id, subscription);
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
                Main.getConfig().removeAndDelteConfiguration(subscription.getID());
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
        ArrayList<String> removals = new ArrayList<>();
        for (RSSSubscription feed: feeds) {
            try {
                feed.update();
            } catch (NullPointerException e) {
                //no url
                e.printStackTrace();
                removals.add(feed.getID());
            }
        }

        for (String s: removals) {
            removeFeed(s);
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

}

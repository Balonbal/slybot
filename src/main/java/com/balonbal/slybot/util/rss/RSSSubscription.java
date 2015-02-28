package com.balonbal.slybot.util.rss;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.config.Config;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import org.apache.commons.lang3.ArrayUtils;
import org.pircbotx.Colors;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class RSSSubscription implements Config {

    private URL feedURL;
    private ArrayList<String> subscribers;
    private String ID;
    private String title;
    private long lastCheck;
    private long lastDate;

    private File savefile;

    public RSSSubscription(String id) {
        ID = id;
    }

    public RSSSubscription(URL url, String id, ArrayList<String> subscriber) {
        SyndFeed feed = RSSUtil.getElements(url);

        if (feed != null) {
            feedURL = url;
            ID = id;

            title = feed.getTitle();
            //Assume the first element is the newest and get the date
            lastDate = feed.getEntries().get(0).getPublishedDate().getTime();
            lastCheck = System.currentTimeMillis();

            subscribers = subscriber;

            System.out.println("Successfully added RSS: " + url.toString());
        } else {
            System.out.println("Unable to add RSS: " + url.toString());
        }
    }

    public void update() {
        if (feedURL == null) {
            System.out.println("ERROR - URL for id " + ID + " was not found, removing feed.");
            throw new NullPointerException("URL is null");
        }
        SyndFeed feed = RSSUtil.getElements(feedURL);
        lastCheck = System.currentTimeMillis();

        //Check if there is anything new to add
        if (feed.getEntries().get(0).getPublishedDate().getTime() > lastDate) {
            SyndEntry[] entries = RSSUtil.getNewEntries(feed.getEntries(), lastDate);
            ArrayUtils.reverse(entries);

            notifySubscribers(entries);

            //Update time
            lastDate = feed.getEntries().get(0).getPublishedDate().getTime();
        }

    }

    public void notifySubscribers(SyndEntry[] entries) {
        for (SyndEntry entry: entries) {
            for (String destination: subscribers) {
                Main.getBot().sendMessage(destination, String.format("[" + Colors.BOLD + "RSS - %s" + Colors.NORMAL +"]" +
                        " [" + Colors.BOLD + "%s" + Colors.NORMAL + "]" +
                        Colors.BLUE + Colors.BOLD + " %s" + Colors.NORMAL + ": " +
                        Colors.DARK_GREEN + "%s" + Colors.NORMAL +
                        " (" + Colors.OLIVE + Colors.BOLD + "%s" + Colors.NORMAL + ")",
                        title, entry.getPublishedDate().toString(), entry.getAuthor(), entry.getTitle(), entry.getLink()));
            }
        }
    }

    @Override
    public void updateSetting(String key, Object value) {
        switch (key) {
            case "id": ID = (String) value; break;
            case "title": title = (String) value; break;
            case "url":
                try {
                    feedURL = new URL((String) value);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;
            case "lastCheck": lastCheck = (long) value; break;
            case "lastDate": lastDate = (long) value; break;
            case "subscribers": subscribers = (ArrayList<String>) value; break;
        }
    }

    @Override
    public void appendSetting(String key, Object value) {
        if (key.equals("subscribers")) {
            subscribers.add((String) value);
        }
    }

    @Override
    public void removeSetting(String key, Object value) {
        if (key.equals("subscribers")) {
            subscribers.remove(value);
        }
    }

    @Override
    public HashMap<String, Object> getSaveValues() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("id", ID);
        map.put("title", title);
        map.put("url", feedURL.toString());
        map.put("lastCheck", lastCheck);
        map.put("lastDate", lastDate);
        map.put("subscribers", subscribers);

        return map;
    }

    @Override
    public void setSaveLocation(File file) {
        savefile = file;
    }

    @Override
    public File getSaveLocation() {
        return savefile;
    }

    public String getID() {
        return ID;
    }

    public URL getURL() {
        return feedURL;
    }

    public boolean isSubscribed(String subscriber) {
        return subscribers.contains(subscriber);
    }

    public void unsubscribe(String subscriber) {
        if (isSubscribed(subscriber)) {
            //remove the subscriber
            subscribers.remove(subscriber);

            //If there are no more subscribers, delete the feed
            if (subscribers.size() == 0) {
                Main.getBot().getRssManager().removeFeed(ID);
            }
        }
    }
}

package com.balonbal.slybot.util.rss;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RSSUtil {

    /** Read RSS/atom from an url
     *
     * @param feedURL the rss link to be read
     * @return a SyndFeed object of the feed
     */
    public static SyndFeed getElements(URL feedURL) {
        try {
            SyndFeedInput feedInput = new SyndFeedInput();

            return feedInput.build(new XmlReader(feedURL));
        } catch (FeedException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /** Read RSS/atom from file
     *
      * @param rssFeed file to read
     * @return a SyndFeed object of the feed
     */
    public static SyndFeed getElements(File rssFeed) {
        try {
            SyndFeedInput feedInput = new SyndFeedInput();

            return feedInput.build(rssFeed);
        } catch (FeedException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static SyndEntry[] getNewEntries(List<SyndEntry> entries, long afterDate) {
        ArrayList<SyndEntry> list = new ArrayList<>();

        //Find all entries after the given time
        for (SyndEntry entry: entries) {
            if (entry.getPublishedDate().getTime() > afterDate) {
                list.add(entry);
            }
        }

        return list.toArray(new SyndEntry[list.size()]);
    }

}

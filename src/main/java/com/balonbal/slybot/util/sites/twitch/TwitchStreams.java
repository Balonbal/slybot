package com.balonbal.slybot.util.sites.twitch;

import java.util.ArrayList;
import java.util.HashMap;

public class TwitchStreams {

    ArrayList<HashMap<String, Object>> streams;
    int _total;
    HashMap<String, Object> _links;

    public ArrayList<HashMap<String, Object>> getStreams() {
        return streams;
    }

    public int get_total() {
        return _total;
    }

    public HashMap<String, Object> get_links() {
        return _links;
    }

    @Override
    public String toString() {
        return streams.toString() + "\nTotal streams: " + _total + "\n" + _links.toString();
    }

}

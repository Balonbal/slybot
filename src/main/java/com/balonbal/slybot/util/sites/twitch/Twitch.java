package com.balonbal.slybot.util.sites.twitch;

import com.balonbal.slybot.lib.Reference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class Twitch {

    private ArrayList<TwitchSubscription> subscriptions;

    public Twitch() {
        subscriptions = new ArrayList<>();
    }

    public void checkSubscriptions() {
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

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

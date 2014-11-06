package com.balonbal.slybot.config;

import com.balonbal.slybot.core.SlyConfiguration;
import com.balonbal.slybot.lib.Reference;
import com.balonbal.slybot.lib.Settings;
import com.balonbal.slybot.lib.Strings;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BotConfig implements Config {

    File save;

    @Override
    @SuppressWarnings("unchecked")
    public void updateSetting(String key, Object value) {
        if (key.equals(Reference.CONFIG_ALIASES)) Settings.aliases = (HashMap<String, String>) value;
        else if (key.equals(Reference.CONFIG_BOTNICK)) Settings.botnick = String.valueOf(value);
        else if (key.equals(Reference.CONFIG_BOTOPS)) Settings.botops = (ArrayList<String>) value;
        else if (key.equals(Reference.CONFIG_BOTPASS)) Settings.operatorpass = String.valueOf(value);
        else if (key.equals(Reference.CONFIG_CHANNELS)) Settings.channels = (ArrayList<String>) value;
        else if (key.equals(Reference.CONFIG_MUTED_CHANNELS)) Settings.mutedChannels = (ArrayList<String>) value;
        else if (key.equals(Reference.CONFIG_NETWORK)) Settings.network = String.valueOf(value);
        else if (key.equals(Reference.CONFIG_NICKPASS)) Settings.nickpass = String.valueOf(value);
        else if (key.equals(Reference.CONFIG_OWNER)) Settings.owner = String.valueOf(value);
        else System.out.println("Unknown config key: " + key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void appendSetting(String key, Object value) {
        if (key.equals(Reference.CONFIG_ALIASES)) {
            HashMap<String, String> newAlias = (HashMap<String, String>) value;
            for (String s: newAlias.keySet()) {
                Settings.aliases.put(s, newAlias.get(s));
            }
        } else if (key.equals(Reference.CONFIG_BOTOPS)) {
            Settings.botops.add(String.valueOf(value));
        } else if (key.equals(Reference.CONFIG_CHANNELS)) {
            Settings.channels.add(String.valueOf(value));
        } else if (key.equals(Reference.CONFIG_MUTED_CHANNELS)) {
            Settings.mutedChannels.add(String.valueOf(value));
        }
    }

    @Override
    public void removeSetting(String key, Object value) {
        if (key.equals(Reference.CONFIG_ALIASES)) {
            if (Settings.aliases.containsKey(String.valueOf(value))) {
                Settings.aliases.remove(String.valueOf(value));
            }
        } else if (key.equals(Reference.CONFIG_BOTOPS)) {
            Settings.botops.remove(String.valueOf(value));
        } else if (key.equals(Reference.CONFIG_CHANNELS)) {
            Settings.channels.remove(String.valueOf(value));
        } else if (key.equals(Reference.CONFIG_MUTED_CHANNELS)) {
            Settings.mutedChannels.remove(String.valueOf(value));
        }
    }

    @Override
    public HashMap<String, Object> getSaveValues() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(Reference.CONFIG_ALIASES, Settings.aliases);
        map.put(Reference.CONFIG_BOTNICK, Settings.botnick);
        map.put(Reference.CONFIG_BOTOPS, Settings.botops);
        map.put(Reference.CONFIG_CHANNELS, Settings.channels);
        map.put(Reference.CONFIG_MUTED_CHANNELS, Settings.mutedChannels);
        map.put(Reference.CONFIG_NETWORK, Settings.network);
        map.put(Reference.CONFIG_OWNER, Settings.owner);
        map.put(Reference.CONFIG_BOTPASS, Settings.operatorpass);
        return map;
    }

    @Override
    public void setSaveLocation(File file) {
        save = file;
    }

    @Override
    public File getSaveLocation() {
        return save;
    }
}

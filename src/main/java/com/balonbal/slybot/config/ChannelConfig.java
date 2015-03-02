package com.balonbal.slybot.config;

import com.balonbal.slybot.lib.Reference;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ChannelConfig implements Config{

    File file;

    String channelName;
    ArrayList<String> triggers;
    HashMap<String, Integer> permissionsMap;
    HashMap<String, String> strings;

    public ChannelConfig(String channel) {
        channelName = channel;

        triggers = new ArrayList<>();
        permissionsMap = new HashMap<>();
        strings = new HashMap<>();
    }

    @Override
    public void updateSetting(String key, Object value) {
        try {
            switch (key) {
                case Reference.CONFIG_CHANNEL_NAME:
                    channelName = String.valueOf(value);
                    break;
                case Reference.CONFIG_CHANNEL_PERMISSIONS:
                    permissionsMap = (HashMap<String, Integer>) value;
                    break;
                case Reference.CONFIG_CHANNEL_STRINGS:
                    strings = (HashMap<String, String>) value;
                    break;
                case Reference.CONFIG_CHANNEL_TRIGGERS: triggers = (ArrayList<String>) value; break;
            }
        } catch (ClassCastException e) {
            System.out.println("Loaded illegal value for key " + key + ": " + e.toString());
        }
    }

    @Override
    public void appendSetting(String key, Object value) {
        switch (key) {
            case Reference.CONFIG_CHANNEL_TRIGGERS: triggers.add(String.valueOf(value)); break;
        }
    }

    @Override
    public void removeSetting(String key, Object value) {
        switch (key) {
            case Reference.CONFIG_CHANNEL_TRIGGERS: triggers.remove(String.valueOf(value)); break;
        }
    }

    @Override
    public HashMap<String, Object> getSaveValues() {
        HashMap<String, Object> map = new HashMap<>();

        map.put(Reference.CONFIG_CHANNEL_NAME, channelName);
        map.put(Reference.CONFIG_CHANNEL_PERMISSIONS, permissionsMap);
        map.put(Reference.CONFIG_CHANNEL_STRINGS, strings);

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

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public void updatePermission(String command, int value) {
        //This will also override previous values
        permissionsMap.put(command, value);
    }

    public int getPermission(String command) {
        return permissionsMap.containsKey(command) ? permissionsMap.get(command) : -1;
    }

    public void updateString(String string, String value) {
        strings.put(string, value);
    }

    public String getString(String string) {
        return strings.containsKey(string) ? strings.get(string) : "";
    }

    public void addTrigger(String trigger) {
        triggers.add(trigger);
    }

    public void removeTrigger(String trigger) {
        triggers.remove(trigger);
    }
}

package com.balonbal.slybot.config;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.commands.Command;
import com.balonbal.slybot.lib.Reference;

import java.io.File;
import java.util.HashMap;
import java.util.regex.Pattern;

public class ChannelConfig implements Config{

    File file;

    String channelName;
    String trigger;
    Pattern triggerRegex;

    HashMap<String, Integer> permissionsMap;
    HashMap<String, String> strings;

    public ChannelConfig(String channel) {
        channelName = channel;

        trigger = "";
        permissionsMap = new HashMap<>();
        strings = new HashMap<>();

        buildDefaults();
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
                case Reference.CONFIG_CHANNEL_TRIGGERS:
                    trigger = String.valueOf(value);
                    triggerRegex = Pattern.compile(trigger.replaceAll("\\$BOTNICK", Main.getBot().getNick()), Pattern.CASE_INSENSITIVE);
                    break;
            }
        } catch (ClassCastException e) {
            System.out.println("Loaded illegal value for key " + key + ": " + e.toString());
        }
    }

    @Override
    public void appendSetting(String key, Object value) {

    }

    @Override
    public void removeSetting(String key, Object value) {

    }

    @Override
    public HashMap<String, Object> getSaveValues() {
        HashMap<String, Object> map = new HashMap<>();

        map.put(Reference.CONFIG_CHANNEL_NAME, channelName);
        map.put(Reference.CONFIG_CHANNEL_TRIGGERS, trigger);
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

    public void buildDefaults() {
        //Build permission defaults
        for (Command c: Main.getCommandListener().getCommandHandler().getCommands()) {
            if (!permissionsMap.containsKey(c.getTrigger())) {
                permissionsMap.put(c.getTrigger(), c.requiresOP());
            }
        }
        //Build trigger defaults
        if (trigger.equals("")) {
            trigger = Reference.PREFIX_REGEX;
            triggerRegex = Pattern.compile(trigger.replaceAll("\\$BOTNICK", Main.getBot().getNick()), Pattern.CASE_INSENSITIVE);
        }
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

    public HashMap<String, Integer> getPermissionsMap() {
        return permissionsMap;
    }

    public void updateString(String string, String value) {
        strings.put(string, value);
    }

    public String getString(String string) {
        return strings.containsKey(string) ? strings.get(string) : "";
    }

    public Pattern getTrigger() {
        return triggerRegex;
    }

    public String getTriggerString() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        System.out.println("Building trigger for nick: " + Main.getBot().getNick());
        this.trigger = trigger;
        triggerRegex = Pattern.compile(trigger.replaceAll("\\$BOTNICK", Main.getBot().getNick()), Pattern.CASE_INSENSITIVE);
    }

    public void setPermission(String trigger, int level) {
        //Also overrides any previous value, be careful
        permissionsMap.put(trigger, level);
    }
}

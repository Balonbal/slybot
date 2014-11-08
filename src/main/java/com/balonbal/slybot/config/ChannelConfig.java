package com.balonbal.slybot.config;

import com.balonbal.slybot.lib.Strings;
import org.pircbotx.Channel;

import java.io.File;
import java.util.HashMap;

public class ChannelConfig implements Config{

    @Override
    public void updateSetting(String key, Object value) {

    }

    @Override
    public void appendSetting(String key, Object value) {

    }

    @Override
    public void removeSetting(String key, Object value) {

    }

    @Override
    public HashMap<String, Object> getSaveValues() {
        return null;
    }

    @Override
    public void setSaveLocation(File file) {

    }

    @Override
    public File getSaveLocation() {
        return null;
    }
}

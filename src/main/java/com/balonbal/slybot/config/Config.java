package com.balonbal.slybot.config;

import java.io.File;
import java.util.HashMap;

public interface Config {

    /**
     * Update an existing key with a new value
     * @param key the key for the value to be updated
     * @param value the new value for the key
     */
    public abstract void updateSetting(String key, Object value);

    /**
     * Used to add to an existing setting without replacing it
     * @param key the setting key
     * @param value the value to be appended
     */
    public abstract void appendSetting(String key, Object value);

    /**
     * Used to remove part of a setting without removing the whole setting
     * @param key the key to replace
     * @param value the value to remove
     */
    public abstract void removeSetting(String key, Object value);

    /**
     * Get the values used in the config for saving
     * @return a hashmap containing all the necessary keys and values
     */
    public abstract HashMap<String, Object> getSaveValues();

    public abstract void setSaveLocation(File file);

    public abstract File getSaveLocation();
}

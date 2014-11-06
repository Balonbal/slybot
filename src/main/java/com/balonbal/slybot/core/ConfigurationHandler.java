package com.balonbal.slybot.core;

import com.balonbal.slybot.config.Config;
import com.balonbal.slybot.lib.Reference;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class ConfigurationHandler {

    public HashMap<String, Config> configurations;

    public ConfigurationHandler() {
        configurations = new HashMap<String, Config>();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                saveAll();
            }
        };

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, System.currentTimeMillis() + 500000, Reference.CONFIG_AUTOSAVE_FREQUENCY);
    }

    public void addConfiguration(File file, String id, Config config) {
        if (file.exists()) {
            try {
                //Try to load configuration from file
                HashMap<String, Object> map = (HashMap<String, Object>) load(file);

                for (String s: map.keySet()) {
                    config.updateSetting(s, map.get(s));
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        config.setSaveLocation(file);
        configurations.put(id, config);
    }

    public Config getConfig(String id) {
        if (configurations.containsKey(id)) {
            return configurations.get(id);
        }

        return null;
    }

    private Object load(File f) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        return yaml.load(new FileInputStream(f));
    }

    public void save(Config config) {
        save(config.getSaveLocation(), config);
    }

    private void save(File f, Config config) {
        save(f, config.getSaveValues());
    }

    private void save(File f, Object o) {
        Yaml yaml = new Yaml();
        try {
            yaml.dump(o, new FileWriter(f));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveAll() {
        System.out.println("Starting full save..");

        for (String s: configurations.keySet()) {
            save(configurations.get(s));
        }

        System.out.println("Save completed.");
    }

}

package com.balonbal.slybot.core;

import com.balonbal.slybot.config.Config;
import com.balonbal.slybot.lib.Settings;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class ConfigurationHandler {

    public HashMap<String, Config> configurations;
    private Timer timer;

    public ConfigurationHandler() {
        configurations = new HashMap<>();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                saveAll();
            }
        };

        timer = new Timer(true);
        //Schedule autosave
        timer.scheduleAtFixedRate(task, 500000, Settings.configAutosaveFrequency);

    }

    public void stopAutoSave() {
        timer.cancel();
    }

    public void updateSaveFrequency(long newFrequency) {
        stopAutoSave();
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                saveAll();
            }
        }, newFrequency, newFrequency);
    }

    public void addConfiguration(File file, String id, Config config) {
        if (file.exists()) {
            try {
                //Try to load configuration from file
                loadFromFile(file, config);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        config.setSaveLocation(file);
        configurations.put(id, config);
    }

    public void loadFromFile(String configId) throws FileNotFoundException {
        if (configurations.containsKey(configId)) loadFromFile(configurations.get(configId).getSaveLocation(), configurations.get(configId));
    }

    public void loadFromFile(File file, Config config) throws FileNotFoundException {
        HashMap<String, Object> map = (HashMap<String, Object>) load(file);

        for (String s: map.keySet()) {
            config.updateSetting(s, map.get(s));
        }
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

    public void save(String id) { if(configurations.containsKey(id)) save(configurations.get(id));}

    public void save(Config config) {
        save(config.getSaveLocation(), config);
    }

    private void save(File f, Config config) {
        save(f, config.getSaveValues());
    }

    private void save(File f, Object o) {
        if (f.getParent() != null && !f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }

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

    public void removeConfiguration(String id) {
        if (!configurations.containsKey(id)) return;
        configurations.remove(id);
    }

    public void removeAndDeleteConfiguration(String id) {
        if (!configurations.containsKey(id)) return;
        Config toDelete = configurations.get(id);
        toDelete.getSaveLocation().delete();
        configurations.remove(id);
    }

}

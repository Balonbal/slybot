package com.balonbal.slybot.core;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public abstract class SlyConfiguration {

	public static HashMap<String, String> configuration;
	public File file;
	
	public SlyConfiguration(String filePath) {
		this(new File(filePath));
	}
	
	public SlyConfiguration(File f) {
		file = f;
	}
	public void initialize() {
		configuration = new HashMap<String, String>();

		//Create the hashmap 
		createSettings();
		updateSettings();
		
		try {
			load();
		} catch(FileNotFoundException e) {
			System.out.println("No settings found. Creating default.");
		}
		
	}
	
	public abstract void updateSettings();

	private void load() throws FileNotFoundException {
		//Check if the file already exists, if not throw exception
		if (!file.isFile()) {
			throw new FileNotFoundException();
		}
		
		Scanner scan = new Scanner(file);

		try {

		String cfg = "";

		while (scan.hasNext()) {
		//Sort all lines of configuration to one string. Separate options with ";;"
			cfg = cfg.concat(scan.nextLine() + ";");
		}

		System.out.println("Successfully loaded configuration from file.");

		Set<String> set = configuration.keySet();
		HashMap<String, String> newConfig = new HashMap<String, String>();

		for (String s: set) {
			if (cfg.contains(s)) {
				//Skin string to start with our option
				String setting = cfg.substring(cfg.indexOf(s));
				//And end at the next ";"
				setting = setting.substring(0, setting.indexOf(";"));

				//Update the current setting to the loaded value
				newConfig.put(s, setting.substring(setting.lastIndexOf(": ")+2));
			}
		}

		Set<String> newSet = newConfig.keySet();
		//Update the configuration map
		for (String s: newSet) {
			changeSetting(s, newConfig.get(s));
		}

		} catch(Exception e) {
			System.out.println(file.getAbsolutePath() + " was corrupted, restoring default settings");

			//If something goes wrong, delete the file and create new ones from the defaults
			file.delete();
			createSettings();
			saveSettings();
		}

		//Close the file
		scan.close();
		saveSettings();
	}

	public abstract void createSettings();

	private void saveSettings() {

		if (!file.exists()) {
			try {
				file.createNewFile();
				} catch (Exception e) {
					System.out.println("ERROR CREATING SAVEFILE!");
				}
			}

			try {
				//Open the file in a buffered writer
				FileWriter filestream = new FileWriter(file);
				BufferedWriter out = new BufferedWriter(filestream);
			
				Set<String> set = configuration.keySet();

				for (String s: set) {
					out.write(s + ": " + configuration.get(s) + "\n");
				}

				//Close the file
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

        public void addSetting(String key, String[] defaultVal, String splitter) {
            addSetting(key, StringUtils.join(defaultVal, splitter));
        }

		public void addSetting(String key, String defaultVal) {
			configuration.put(key, defaultVal);
		}

		public void changeSetting(String key, String newValue) {

			//Check if the key exists, if so; remove it
			if (configuration.containsKey(key) && !configuration.get(key).equals(newValue)) {
				configuration.remove(key);
			}
			//Put in the new value
			configuration.put(key, newValue);

			//And save the settings
			saveSettings();
			updateSettings();
		}
		
		public void appendSetting(String key, String splitter, String addValue) {

			String s = "";
			//Check if the key exists, if so; remove it
			if (configuration.containsKey(key)) {
				
				s = (configuration.get(key).equals("") ? addValue : configuration.get(key) + splitter + addValue);
				configuration.remove(key);
			}
			
			//Put in the new value
			configuration.put(key, s);

			//And save the settings
			saveSettings();
			updateSettings();
		}

		public static String getSetting(String key) {
			return configuration.get(key);
		}

	}
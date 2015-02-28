package com.balonbal.slybot.lib;

import java.util.ArrayList;
import java.util.HashMap;

public class Settings {

    public static String owner = "";
    public static ArrayList<String> botops = new ArrayList<String>();
    public static String botnick = "";
    public static String nickpass = "";
    public static String network = "";
    public static int port = 6667;
    public static boolean ssl = false;
    public static String serverPass = "";
    public static ArrayList<String> channels = new ArrayList<String>();
    public static String operatorpass = "";
    public static ArrayList<String> loggedChannels = new ArrayList<String>();
    public static ArrayList<String> mutedChannels = new ArrayList<String>();
    public static HashMap<String, String> aliases = new HashMap<String, String>();
    public static HashMap<String, HashMap<String, String>> userEvents = new HashMap<String, HashMap<String, String>>();
    public static long configAutosaveFrequency = 2*60*60*1000; //Every two hours
    public static long rssUpdateFrequency = 30*60*1000; //Every 30 minutes

    public static boolean suppressOutput = false;
}

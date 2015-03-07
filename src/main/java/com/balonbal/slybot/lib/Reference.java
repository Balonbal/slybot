package com.balonbal.slybot.lib;

public class Reference {

    /* BOT CONSTANTS */
    public static final String PREFIX_REGEX = "($BOTNICK(, |: | )|\\.)";
	
	public static final int ACCEPT_TIMEOUT = 60;
	public static final int TTT_NEXT_TURN_TIMEOUT = 180;

    /* PERMISSION CONSTANTS */
    public static final int REQUIRES_OP_NONE = -1;
    public static final int REQUIRES_OP_ANY = 0;
    public static final int REQUIRES_OP_CHANNEL = 1;
    public static final int REQUIRES_OP_BOT = 2;
    public static final int REQUIRES_OP_BOTH = 3;

    /* MAL API CONSTANTS */

    //URLs
    public static final String MAL_VERIFY_CREDENTIALS = "http://myanimelist.net/api/account/verify_credentials.xml";
    public static final String MAL_ANIME_SEARCH_BASE = "http://myanimelist.net/api/anime/search.xml?q=";

    //XML tags
    public static final String MAL_ANIME_ID = "id";
    public static final String MAL_ANIME_NAME = "name";
    public static final String MAL_ANIME_ENGLISH_NAME = "english";
    public static final String MAL_ANIME_SYNONYMS = "synonyms";
    public static final String MAL_ANIME_NUM_EPISODES = "episodes";
    public static final String MAL_ANIME_TYPE = "type";
    public static final String MAL_ANIME_STATUS = "status";
    public static final String MAL_ANIME_START_DATE = "start_date";
    public static final String MAL_ANIME_END_DATE = "end_date";
    public static final String MAL_ANIME_SYNOPSIS = "synopsis";
    public static final String MAL_ANIME_IMAGE = "image";


    /* CONFIGURATION CONSTANTS */
    public static final String CONFIG_BASE_DIR = "config/";
    public static final String CONFIG_CHANNEL_FILE = CONFIG_BASE_DIR + "$NETWORK/$CHANNEL.yaml";

    public static final String CONFIG_CHANNEL_NAME = "channelName";
    public static final String CONFIG_CHANNEL_PERMISSIONS = "channelPermissions";
    public static final String CONFIG_CHANNEL_STRINGS = "channelStrings";
    public static final String CONFIG_CHANNEL_TRIGGERS = "channelTriggers";

    public static final String LOG_BASE_DIR = "logs/";
    public static final String LOG_CHANNEL_FILE = LOG_BASE_DIR + "$NETWORK/$CHANNEL.log";

    public static final String CONFIG_ALIASES = "aliases";
    public static final String CONFIG_AUTOSAVE = "autosaveFrequency";
    public static final String CONFIG_RSS_UPDATE = "rssUpdateFrequency";
    public static final String CONFIG_BOTNICK = "botnick";
    public static final String CONFIG_BOTOPS = "botops";
    public static final String CONFIG_BOTPASS = "operatorPass";
    public static final String CONFIG_CHANNELS = "channels";
    public static final String CONFIG_MUTED_CHANNELS = "mutedChannels";
    public static final String CONFIG_NETWORK = "network";
    public static final String CONFIG_PORT = "port";
    public static final String CONFIG_SERVERPASS = "serverpass";
    public static final String CONFIG_SSL = "ssl";
    public static final String CONFIG_NICKPASS = "nickpass";
    public static final String CONFIG_OWNER = "owner";

    public static final String CONFIG_BOTCONFIG_ID = "botConfig";
}

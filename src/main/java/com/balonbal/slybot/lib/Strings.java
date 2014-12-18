package com.balonbal.slybot.lib;

import java.util.regex.Pattern;

public class Strings {

    public static final Pattern youtube = Pattern.compile("(?:https?)://(www\\.|)youtu(\\.be|be\\.com)");
    public static final String YOUTUBE_BASE = "https://gdata.youtube.com/feeds/api/";
    public static final String YOUTUBE_VIDEO = YOUTUBE_BASE + "videos/";
    public static final String YOUTUBE_PLAYLIST = YOUTUBE_BASE + "playlists/";

}

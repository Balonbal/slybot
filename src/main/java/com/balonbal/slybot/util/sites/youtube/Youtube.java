package com.balonbal.slybot.util.sites.youtube;

import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.lib.Reference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Youtube {

    private static final Pattern videoCode = Pattern.compile("(?<=(v=|/))(?<!user/)[\\w\\d\\-_]{11}");
    private static final Pattern playlistCode = Pattern.compile("(?<=(list=))[\\w\\d\\-_]+");
    File video, playlist;

	public Youtube() {

    }

    public void parseVideoInfo(String url, Event<SlyBot> event) throws IllegalArgumentException {
	    //Remove any extra parameters
        Matcher matcher = videoCode.matcher(url);
        Matcher playlistMatcher = playlistCode.matcher(url);
        String playlistId = null;
        String videoId = null;

        if (playlistMatcher.find()) {
            playlistId = playlistMatcher.group();
        }

        if (matcher.find()) {
            videoId = url.substring(matcher.start(), matcher.end());
        }

        try {
            //Create a new temp file
            video = File.createTempFile("yt_video", null);
            playlist = File.createTempFile("yt_playlist", null);

            try {
                //Download the files
                if (videoId != null) downloadToFile(new URL(Reference.YOUTUBE_VIDEO_URL.replaceAll("\\$KEY", Reference.YOUTUBE_API_KEY).replaceAll("\\$ID", videoId)), video);
                if (playlistId != null) downloadToFile(new URL(Reference.YOUTUBE_PLAYLIST_URL.replaceAll("\\$KEY", Reference.YOUTUBE_API_KEY).replaceAll("\\$PLAYLIST_ID", playlistId)), playlist);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HashMap<String, String> map = (videoId == null ? new HashMap<String, String>() : parseJsonFile(video));
            if (videoId != null) map.put("id", videoId);
            if (playlistId != null) {
                //Add playlist info (if any)
                map.put("playlist_id", playlistId);
                appendPlaylistInfo(map, playlist);
            }

            printInfo(event, map, videoId != null, playlistId != null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //Always delete the file
            video.delete();
            playlist.delete();
        }
	}

    private void downloadToFile(URL url, File file) {

        long then = System.currentTimeMillis();
        System.out.println("Fetching file from: " + url.getHost() + url.getPath() + url.getFile());

        try {
            //Open the remote file for reading
            InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
            BufferedReader reader = new BufferedReader(inputStreamReader);

            //Open the local file for writing
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            //Write to file
            String s;
            while ((s = reader.readLine()) != null) {
                writer.write(s);
            }

            //Clean up
            writer.close();
            fileWriter.close();
            reader.close();
            inputStreamReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Log the time taken
        long now = System.currentTimeMillis();
        System.out.println("Fetched file in " + (now - then) + "ms");
    }

    public void printInfo(Event<SlyBot> event, HashMap<String, String> items, boolean video, boolean playlist) {

        if (video) {
            String id = items.get("id");
            String creator = items.get("creator");
            String title = items.get("title");
            String views = items.get("views");
            String duration = items.get("duration");
            String date = items.get("date");
            String likes = items.get("likeCount");
            String disLikes = items.get("dislikeCount");

            //Format the view counter to separate each thousand
            String viewCount = String.format("%,d", Long.parseLong(views));
            likes = String.format("%,d", Long.parseLong(likes));
            disLikes = String.format("%,d", Long.parseLong(disLikes));

            StringBuilder time = new StringBuilder();

            //Build time string
            int hour = 0, minute = 0, seconds = 0;
            if (duration.contains("H")) {
                hour = Integer.parseInt(duration.substring(2, duration.indexOf("H")));
                time.append(hour);
                time.append(":");
            }
            if (duration.contains("M")) {
                minute = Integer.parseInt(duration.substring(duration.contains("H") ? duration.indexOf("H") + 1 : 2, duration.indexOf("M")));
            }
            if (duration.contains("S")) {
                seconds = Integer.parseInt(duration.substring(duration.contains("M") ? duration.indexOf("M") + 1 : duration.contains("H") ? duration.indexOf("H") + 1 : 2, duration.indexOf("S")));
            }

            if (minute < 10) time.append("0");
            time.append(minute);
            time.append(":");
            if (seconds < 10) time.append("0");
            time.append(seconds);

            //Convert date to a displayable format depending on locale
            date = date.replaceAll("(\\d{4})\\-(\\d{2})\\-(\\d{2})T((\\d{2}):(\\d{2}):(\\d{2}))(.*)", "$4 $3. $2 $1");
            DateFormat format = new SimpleDateFormat("kk:mm:ss dd. MM yyyy");

            try {
                date = format.parse(date).toString();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //Send the results for the video back to the channel/user
            event.getBot().reply(event, "[http://youtu.be/" + Colors.PURPLE + id + Colors.NORMAL + "] - " +
                    Colors.BOLD + Colors.BLUE + creator + Colors.NORMAL + ": " + Colors.DARK_GREEN + title + Colors.NORMAL +
                    " [ " + Colors.BOLD + time.toString() + Colors.NORMAL + " ]" +
                    " [ v: " + Colors.BOLD + viewCount + Colors.NORMAL + " ]" +
                    " [ U: " + Colors.GREEN + Colors.BOLD + likes + Colors.NORMAL + " | D: " + Colors.RED + Colors.BOLD + disLikes + Colors.NORMAL + " ]" +
                    " [ UL: " + Colors.BOLD + date + Colors.NORMAL + " ]");
        }
        if (playlist) {
            event.getBot().reply(event, "[Playlist - http://youtube.com/playlist?list=" + Colors.PURPLE + items.get("playlist_id") + Colors.NORMAL + "] - " +
                    Colors.BOLD + Colors.BLUE + items.get("playlist_owner") + Colors.NORMAL + ": " + Colors.DARK_GREEN + items.get("playlist_title") + Colors.NORMAL +
                    " [ size: " + Colors.BOLD + items.get("playlist_size").substring(0, items.get("playlist_size").indexOf(".")) + Colors.NORMAL + " ]");
        }
    }

    private void appendPlaylistInfo(HashMap<String, String> map, File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            Gson gson = new GsonBuilder().create();

            YoutubeInfo ytinfo = gson.fromJson(reader, YoutubeInfo.class);
            LinkedTreeMap<String, Object> info = ytinfo.getItems().get(0);

            map.put("playlist_owner", ((LinkedTreeMap<String, String>) info.get("snippet")).get("channelTitle"));
            map.put("playlist_title", ((LinkedTreeMap<String, String>) info.get("snippet")).get("title"));
            map.put("playlist_size", String.valueOf(((LinkedTreeMap<String, Object>) info.get("contentDetails")).get("itemCount")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, String> parseJsonFile(File f) {
        HashMap<String, String> items = new HashMap<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(f));

            Gson gson = new GsonBuilder().create();

            YoutubeInfo ytinfo = gson.fromJson(reader, YoutubeInfo.class);

            LinkedTreeMap<String, Object> info = ytinfo.getItems().get(0);
            
            items.put("title", (String) ((LinkedTreeMap<String, Object>) info.get("snippet")).get("title"));
            items.put("views", (String) ((LinkedTreeMap<String, Object>) info.get("statistics")).get("viewCount"));
            items.put("creator", (String) ((LinkedTreeMap<String, Object>) info.get("snippet")).get("channelTitle"));
            items.put("duration", (String) ((LinkedTreeMap<String, Object>) info.get("contentDetails")).get("duration"));
            items.put("date", (String) ((LinkedTreeMap<String, Object>) info.get("snippet")).get("publishedAt"));
            items.put("likeCount", (String) ((LinkedTreeMap<String, Object>) info.get("statistics")).get("likeCount"));
            items.put("dislikeCount", (String) ((LinkedTreeMap<String, Object>) info.get("statistics")).get("dislikeCount"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return items;
    }

    @Deprecated
    private HashMap<String, String> parseXmlFile(File f){
		
		//Store the found values in a hashmap
		
		HashMap<String, String> items = new HashMap<String, String>();
	    try {
	    	 
	    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    	Document doc = dBuilder.parse(f);
	     
	    	doc.getDocumentElement().normalize();
	     
	    	//Get the element "entry"
	    	NodeList nList = doc.getElementsByTagName("entry");
	     
	    	for (int i = 0; i < nList.getLength(); i++) {
	     
	    	    Node nNode = nList.item(i);
	     
	    	    Element eElement = (Element) nNode;

	    	    //Get the stats from the xml document
                try {
                    items.put("title", eElement.getElementsByTagName("media:title").item(0).getChildNodes().item(0).getNodeValue());
                    items.put("views", eElement.getElementsByTagName("yt:statistics").item(0).getAttributes().getNamedItem("viewCount").getNodeValue());
                    items.put("creator", eElement.getElementsByTagName("author").item(0).getChildNodes().item(0).getChildNodes().item(0).getNodeValue());
                    items.put("duration", eElement.getElementsByTagName("yt:duration").item(0).getAttributes().item(0).getNodeValue());
                    items.put("date", eElement.getElementsByTagName("published").item(0).getChildNodes().item(0).getNodeValue());
                    items.put("likeCount", eElement.getElementsByTagName("yt:rating").item(0).getAttributes().getNamedItem("numLikes").getNodeValue());
                    items.put("dislikeCount", eElement.getElementsByTagName("yt:rating").item(0).getAttributes().getNamedItem("numDislikes").getNodeValue());
                }catch (Exception e) {
                    //Should something go wrong, populate the empty fields
                    for (String s: new String[] { "title", "views", "creator", "duration" } ) {
                        if (!items.containsKey(s)) {
                            items.put(s, Colors.RED + "ERROR" + Colors.NORMAL);
                        }
                    }
                }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return items;
	}


}

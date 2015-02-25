package com.balonbal.slybot.util;

import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.lib.Strings;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Youtube {

    private static final Pattern videoCode = Pattern.compile("(?<=(v=|/))(?<!user/)[\\w\\d\\-_]{11}");
    private static final Pattern playlistCode = Pattern.compile("(?<=(list=))[\\w\\d\\-_]+");
    File f;

	public Youtube() {

    }

    public void parseVideoInfo(String url, Event<SlyBot> event) throws IllegalArgumentException {
	    //Remove any extra parameters
        Matcher matcher = videoCode.matcher(url);
        Matcher playlistMatcher = playlistCode.matcher(url);
        String playlist = null;

        if (playlistMatcher.find()) {
            playlist = playlistMatcher.group();
        }

        if (!matcher.find()) throw new IllegalArgumentException("Argument did not contain a valid video code");
        url = url.substring(matcher.start(), matcher.end());

		Random r = new Random();

        //Create a new temp file
		f = new File("tmp_" + r.nextInt(10000));
        File playlistFile = new File("tmp_" + r.nextInt(10000));
        try {
			f.createNewFile();
            if (playlist != null) playlistFile.createNewFile();
        } catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        try {
            downloadToFile(new URL(Strings.YOUTUBE_VIDEO + url), f);
            if (playlist != null) downloadToFile(new URL(Strings.YOUTUBE_PLAYLIST + playlist), playlistFile);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HashMap<String, String> map = parseXmlFile(f);
        map.put("id", url);
        if (playlist != null) {
            map.put("playlist_id", playlist);
            appendPlaylistInfo(map, playlistFile);
            playlistFile.delete();
        }

        printInfo(event, map, playlist != null);

        f.delete();
	}

    private void downloadToFile(URL url, File file) {

        long then = System.currentTimeMillis();
        System.out.println("Fetching file from: " + url.getHost() + url.getPath());

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

    public void printInfo(Event<SlyBot> event, HashMap<String, String> items, boolean playlist) {

        String id = items.get("id");
		String creator = items.get("creator");
		String title = items.get("title");
		String views = items.get("views");
		String duration = items.get("duration");
        String date = items.get("date");
		
		//Format the view counter to separate each thousand
        String viewCount = String.format("%,d", Long.parseLong(views));


        int seconds = Integer.parseInt(duration);
		duration = "";
		//If the time exceeds one hour
		if (seconds > 3600) {
			duration = seconds / 3600 + ":";
			seconds %= 3600;
		}
		duration += ((seconds / 60 > 9) ? (seconds / 60) : "0" + (seconds/60)) + ":" + ((seconds % 60 > 9) ? (seconds % 60) : "0" + (seconds % 60));

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
                " [ " + Colors.BOLD + duration + Colors.NORMAL + " ]" +
                " [ v: " + Colors.BOLD + viewCount + Colors.NORMAL + " ]" +
                " [ UL: " + Colors.BOLD + date + Colors.NORMAL + " ]");

        if (playlist) {
            event.getBot().reply(event, "[Playlist] - " +
                    Colors.BOLD + Colors.BLUE + items.get("playlist_owner") + Colors.NORMAL + ": " + Colors.DARK_GREEN + items.get("playlist_title") + Colors.NORMAL +
                    " [ size: " + Colors.BOLD + items.get("playlist_size") + Colors.NORMAL + " ]");
        }
    }

    private void appendPlaylistInfo(HashMap<String, String> map, File file) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            Document document = builder.parse(file);

            document.getDocumentElement().normalize();

            Element list = (Element) document.getElementsByTagName("feed").item(0);

            map.put("playlist_owner", list.getElementsByTagName("author").item(0).getChildNodes().item(0).getChildNodes().item(0).getNodeValue());
            map.put("playlist_title", list.getElementsByTagName("title").item(0).getChildNodes().item(0).getNodeValue());
            map.put("playlist_size", list.getElementsByTagName("openSearch:totalResults").item(0).getChildNodes().item(0).getNodeValue());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

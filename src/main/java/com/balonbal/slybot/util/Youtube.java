package com.balonbal.slybot.util;

import com.balonbal.slybot.SlyBot;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Youtube {

    File f;
    private static final Pattern videoCode = Pattern.compile("(?<=(v=|/))(?<!user/)[\\w\\d\\-_]{11}");

	public Youtube() {

    }

    public void parseVideoInfo(String url, Event<SlyBot> event) throws IllegalArgumentException {
	    //Remove any extra parameters
        Matcher matcher = videoCode.matcher(url);
        if (!matcher.find()) throw new IllegalArgumentException("Argument did not contain a valid video code");
        url = url.substring(matcher.start(), matcher.end());

		Random r = new Random();
        long then = System.currentTimeMillis();
        System.out.println("Fetching file from https://gdata.youtube.com/feeds/api/videos/" + url);

        //Create a new temp file
		f = new File("tmp_" + r.nextInt(10000));
		try {
			f.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			//Fetch the file from the link
			URL u = new URL("https://gdata.youtube.com/feeds/api/videos/" + url);
			InputStreamReader is = new InputStreamReader(u.openStream());
			BufferedReader dis = new BufferedReader(is);
			
			String s;
			
			//Write to file
			FileWriter filestream = new FileWriter(f);
			BufferedWriter out = new BufferedWriter(filestream);
			
			while ((s = dis.readLine()) != null)  {
				out.write(s);
			}
			
			//Close the outputbufferer
			out.close();
		} catch (IOException e) {
            System.out.println("An error occured while reading file: " + e.toString());
        }
        long now = System.currentTimeMillis();
        System.out.println("Fetched file in " + (now - then) + "ms");

        HashMap<String, String> map = parseXmlFile(f);
        map.put("id", url);

        printInfo(event, map);

        f.delete();
	}
	
	public void printInfo(Event<SlyBot> event, HashMap<String, String> items) {

        String id = items.get("id");
		String creator = items.get("creator");
		String title = items.get("title");
		String views = items.get("views");
		String duration = items.get("duration");
		
		//Format the view counter to separate each thousand
		String viewCount = String.format("%,d", Integer.parseInt(views));
		
		
		int seconds = Integer.parseInt(duration);
		duration = "";
		//If the time exceeds one hour
		if (seconds > 3600) {
			duration = seconds / 3600 + ":";
			seconds %= 3600;
		}
		duration += ((seconds / 60 > 9) ? (seconds / 60) : "0" + (seconds/60)) + ":" + ((seconds % 60 > 9) ? (seconds % 60) : "0" + (seconds % 60));
		
		//Send the results for the video back to the channel/user
		event.getBot().reply(event, "[http://youtu.be/" + Colors.PURPLE + id + Colors.NORMAL + "] - " + Colors.BOLD + Colors.BLUE + creator + Colors.NORMAL + ": " + Colors.DARK_GREEN + title + Colors.NORMAL + " [ " + Colors.BOLD + duration + Colors.NORMAL + " ] [ v: " + Colors.BOLD + viewCount + Colors.NORMAL + " ]");
		
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

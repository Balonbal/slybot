package com.balonbal.slybot.util;

import org.pircbotx.Channel;
import org.pircbotx.Colors;
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
import java.util.logging.Logger;

public class Youtube {

    private static final Logger logger = Logger.getLogger(Youtube.class.getName());
    File f;

	public Youtube(String url) {
		//Check if it's an URL or just the code
		if (url.startsWith("http://") || url.startsWith("https://")) {
			//Get the video code
			if (url.startsWith("http://youtu.be")) {
				url = url.substring(url.indexOf("http://youtu.be")  + 16);
			} else if (url.startsWith("https://youtu.be")) {
				url = url.substring(url.indexOf("https://youtu.be" + 17));
			} else {
				url = url.substring(url.indexOf("v=")+2);
			}
			//Remove any extra parameters
			if (url.contains("&")) {
				url = url.substring(0, url.indexOf("&"));
			}
			if (url.contains(" ")) {
				url = url.substring(0, url.indexOf(" "));
			}
		}
		Random r = new Random();
        logger.info("Fetching file from https://gdata.youtube.com/feeds/api/videos/" + url);

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
            logger.warning("An error occured while reading file: " + e.toString());
        }
		
	}
	
	public void printInfo(Channel c) {
		HashMap<String, String> items = parseXmlFile();
		
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
		
		//Send the results for the video back to the channel
		c.send().message(" [ YouTube.com - " + Colors.BOLD + creator  + Colors.NORMAL + " ] " + title + " [ " + Colors.BOLD + duration + Colors.NORMAL + " ] [ v: " + Colors.BOLD + viewCount + Colors.NORMAL + " ]");
		
	}
	
	private HashMap<String, String> parseXmlFile(){
		
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
    			items.put("title", eElement.getElementsByTagName("media:title").item(0).getChildNodes().item(0).getNodeValue());
    			items.put("views", eElement.getElementsByTagName("yt:statistics").item(0).getAttributes().getNamedItem("viewCount").getNodeValue());
    			items.put("creator", eElement.getElementsByTagName("author").item(0).getChildNodes().item(0).getChildNodes().item(0).getNodeValue());
    			items.put("duration", eElement.getElementsByTagName("yt:duration").item(0).getAttributes().item(0).getNodeValue());
	    	}
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	    return items;
	    }
	
	public void deleteFile() {
		//Remove the file
		f.delete();
	}
	
}

package slybot.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Youtube {
	
	File f;

	public Youtube(String url) {
		//Check if it's an URL or just the code
		if (url.startsWith("http://") || url.startsWith("https://")) {
			//Get the video code
			url = url.substring(url.indexOf("v=")+2);
			//Remove any extra parameters
			if (url.contains("&")) {
				url = url.substring(0, url.indexOf("&"));
			}
			if (url.contains(" ")) {
				url = url.substring(0, url.indexOf(" "));
			}
		}
		Random r = new Random();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
    			items.put("title", eElement.getElementsByTagName("media:title").item(0).getTextContent());
    			items.put("views", eElement.getElementsByTagName("yt:statistics").item(0).getAttributes().getNamedItem("viewCount").getTextContent());
    			items.put("creator", eElement.getElementsByTagName("author").item(0).getChildNodes().item(0).getTextContent());
    			items.put("duration", eElement.getElementsByTagName("yt:duration").item(0).getAttributes().item(0).getTextContent());
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

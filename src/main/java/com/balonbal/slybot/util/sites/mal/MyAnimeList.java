package com.balonbal.slybot.util.sites.mal;

import com.balonbal.slybot.lib.Reference;
import com.sun.xml.internal.fastinfoset.Encoder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.xml.sax.SAXException;
import sun.misc.BASE64Encoder;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class MyAnimeList {

    public static boolean checkLogin(String username, String password) {
        BASE64Encoder encoder = new BASE64Encoder();

        System.out.println("Checking authentication with MAL...");

        String userPwd = encoder.encode((username + ":" + password).getBytes());
        CloseableHttpClient client = HttpClients.custom().build();

        BufferedReader reader = null;
        CloseableHttpResponse response = null;

        try {

            HttpClientContext context = new HttpClientContext();
            BasicCookieStore store = new BasicCookieStore(); //We need cookies to log in..
            context.setCookieStore(store);

            //Retry up to three times

            for (int i = 0; i < 3; i++) {
                HttpGet request = buildRequest(Reference.MAL_VERIFY_CREDENTIALS, userPwd);
                System.out.println("Sending test request: " + request.getRequestLine());

                response = client.execute(request, context);
                reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                String s;

                //While the bufferer is not empty
                while ((s = reader.readLine()) != null) {
                    if (s.contains("<username>")) {
                        return true;
                    }
                    if (s.equals("Invalid credentials")) {
                        return false;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //Clean up
            try {
                if (reader != null) reader.close();
                if (response != null) response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static ArrayList<Anime> searchAnime(String search, String username, String password) {
        HashMap<String, String> map = new HashMap<>();

        CloseableHttpResponse response = null;
        File tempFile = new File("tmp_malSearch" + System.currentTimeMillis());

        try {
            HttpClientContext context = new HttpClientContext();
            BasicCookieStore store = new BasicCookieStore(); //We need cookies to log in..

            context.setCookieStore(store);
            CloseableHttpClient client = HttpClients.custom().build();
            HttpGet request = buildRequest(Reference.MAL_ANIME_SEARCH_BASE + URLEncoder.encode(search, Encoder.UTF_8), getEncodedPassphrase(username, password));

            response = client.execute(request, context);

            InputStreamReader inputStreamReader = new InputStreamReader(response.getEntity().getContent());
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String s;

            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            while ((s = reader.readLine()) != null) {
                //Write out
                writer.write(s + "\n");

                //Inject doctype to fix their xml
                if (s.matches("<\\?xml version=\"\\d\\.\\d\" encoding=\"[\\w\\d\\-]+\"\\?>")) {
                    writer.write("<!DOCTYPE stylesheet [\n<!ENTITY ndash \"&#x2013;\" >\n<!ENTITY mdash \"&#x2014;\">\n]>\n");
                }
            }
            writer.close();
            reader.close();

            //Parse content
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser parser = saxParserFactory.newSAXParser();
            AnimeHandler handler = new AnimeHandler();

            parser.parse(tempFile, handler);

            return handler.getList();
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        } finally {

            //Clean up

            tempFile.delete();
            try {
                if (response != null) response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return new ArrayList<>();
    }

    private static String getEncodedPassphrase(String username, String password) {
        BASE64Encoder encoder = new BASE64Encoder();

        return encoder.encode((username + ":" + password).getBytes());
    }

    private static HttpGet buildRequest(String url, String encodedPassword) {
        HttpGet request = new HttpGet(url);

        //Build headers manually (as it does not seem to work otherwise
        request.setHeader("Authorization", "Basic " + encodedPassword);
        request.addHeader("Host", "myanimelist.net");
        request.addHeader("User-Agent", "curl/7.39.0");
        request.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        request.addHeader("Accept-Encoding", "gzip, deflate");
        request.addHeader("DNT", "1");
        request.addHeader("Connection", "keep-alive");

        return request;
    }

}

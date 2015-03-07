package com.balonbal.slybot.util.sites.mal;

import com.balonbal.slybot.lib.Reference;
import com.sun.xml.internal.fastinfoset.Encoder;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Random;

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

    public static HashMap<String, String> searchAnime(String search, String username, String password) {
        HashMap<String, String> map = new HashMap<>();
        Random random = new Random();

        HttpGet request = buildRequest(Reference.MAL_ANIME_SEARCH_BASE + search, getEncodedPassphrase(username, password));
        File tempFile = new File("tmp_" + random.nextInt(10000));

        HttpResponse response = null;
        CloseableHttpClient client = null;

        try {
            HttpClientContext context = new HttpClientContext();
            BasicCookieStore store = new BasicCookieStore(); //We need cookies to log in..

            context.setCookieStore(store);
            client = HttpClients.custom().build();

            response = client.execute(request, context);

            //TODO Handle response
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    private static String getEncodedPassphrase(String username, String password) {
        BASE64Encoder encoder = new BASE64Encoder();

        return encoder.encode((username + ":" + password).getBytes());
    }

    private static HttpGet buildRequest(String url, String encodedPassword) {
        try {
            url = URLEncoder.encode(url, Encoder.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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

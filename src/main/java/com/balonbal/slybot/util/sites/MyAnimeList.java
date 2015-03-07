package com.balonbal.slybot.util.sites;

import com.balonbal.slybot.lib.Reference;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import sun.misc.BASE64Encoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
                HttpGet request = new HttpGet(Reference.MAL_VERIFY_CREDENTIALS);
                System.out.println("Sending test request: " + request.getRequestLine());

                //Build headers manually (as it does not seem to work otherwise
                request.setHeader("Authorization", "Basic " + userPwd);
                request.addHeader("Host", "myanimelist.net");
                request.addHeader("User-Agent", "curl/7.39.0");
                request.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                request.addHeader("Accept-Encoding", "gzip, deflate");
                request.addHeader("DNT", "1");
                request.addHeader("Connection", "keep-alive");

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



}

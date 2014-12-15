package com.balonbal.slybot.util;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class VersionUtil {

    public static String getRemoteSHA(String githubUser, String githubRepo) {
        return getRemoteSHA(githubUser, githubRepo, "master");
    }

    public static String getRemoteSHA(String githubUser, String githubRepo, String branch) {
        String url = "https://api.github.com/repos/" + githubUser + "/" + githubRepo + "/git/refs/heads/" + branch;
        System.out.println("Fetching github data from: " + url);
        Random r = new Random();
        File f = new File("tmp_" + r.nextInt(10000));

        try {
            URL remote = new URL(url);
            InputStreamReader is = new InputStreamReader(remote.openStream());
            BufferedReader reader = new BufferedReader(is);

            String s = reader.readLine();

            int start = s.indexOf("\"sha\"") + 7;
            int end = s.indexOf("\"", start);
            return s.substring(start, end);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String getLatestCommit(String githubUser, String githubRepo) {
        return getLatestCommit(githubUser, githubRepo, "master");
    }
    public static String getLatestCommit(String githubUser, String githubRepo, String branch) {
        return getCommit(githubUser, githubRepo, getRemoteSHA(githubUser, githubRepo, branch));
    }
    public static String getCommit(String githubUser, String githubRepo, String SHA) {
        String url = "https://api.github.com/repos/" + githubUser + "/" + githubRepo + "/git/commits/" + SHA;
        System.out.println("Fetching github data from: " + url);
        try {
            URL remote = new URL(url);
            InputStreamReader streamReader = new InputStreamReader(remote.openStream());
            BufferedReader reader = new BufferedReader(streamReader);
//Read remote
            String s = reader.readLine();
            int start = s.indexOf("\"message\"") + 11;
            int end = s.indexOf("\"", start);
            return s.substring(start, end);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}

package com.balonbal.slybot.util.stats;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.lib.Reference;
import com.balonbal.slybot.lib.Settings;
import org.pircbotx.hooks.Event;

import java.io.*;
import java.util.HashMap;

public class ChannelStats {

    HashMap<String, Integer> messageCount; //Messages per user
    HashMap<String, Integer> commandCount; //Commands
    int messages; //Total message count

    public ChannelStats(String channel) {

        //Initialize
        messages = 0;
        messageCount = new HashMap<>();
        commandCount = new HashMap<>();

        //Get channel log
        String path = Reference.LOG_CHANNEL_FILE;
        path = path.replaceAll("\\$NETWORK", Settings.network);
        path = path.replaceAll("\\$CHANNEL", channel);
        File logfile = new File(path);

        if (logfile.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(logfile)));

                String s;
                while ((s = reader.readLine()) != null) {
                    String user = s.substring(s.indexOf("<"), s.indexOf(">")); //Get user name
                    messageCount.put(user, messageCount.get(user) == null ? 1 : messageCount.get(user) + 1); //Add to last value or set to 1

                    //Check for command
                    String cmd = Main.getCommandListener().getCommand(s.substring(s.indexOf('>') + 1), channel);
                    if (!cmd.equals("") || cmd.isEmpty()) {
                        commandCount.put(cmd, messageCount.get(cmd) == null ? 1 : commandCount.get(cmd));
                    }

                    messages++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Cache built, read " + messages + " messages");
    }

}

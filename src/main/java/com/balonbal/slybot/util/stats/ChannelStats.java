package com.balonbal.slybot.util.stats;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.lib.Reference;
import com.balonbal.slybot.lib.Settings;
import org.pircbotx.hooks.Event;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ChannelStats {

    HashMap<String, Integer> messageCount; //Messages per user

    public HashMap<String, Integer> getMessageCount() {
        return messageCount;
    }

    public HashMap<String, Integer> getCommandCount() {
        return commandCount;
    }

    public int getMessages() {
        return messages;
    }

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
                    String user = s.substring(s.indexOf("<") + 1, s.indexOf(">")); //Get user name
                    messageCount.put(user, messageCount.get(user) == null ? 1 : messageCount.get(user) + 1); //Add to last value or set to 1

                    //Check for command
                    String cmd = Main.getCommandListener().getCommand(s.substring(s.indexOf(">") + 2), channel);
                    if (!cmd.equals("") && !cmd.isEmpty() ) {
                        commandCount.put(cmd, commandCount.get(cmd) == null ? 1 : commandCount.get(cmd) + 1);
                    }

                    messages++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Cache built, read " + messages + " messages");
    }

    public String getMostActiveUser() {
        int max = 0;
        String highest ="";

        for (String u : messageCount.keySet()) {
                if (messageCount.get(u) > max) {
                    highest = u;
                    max = messageCount.get(u);
                }
        }

        return highest;
    }

    public ArrayList<String> getMostCommands(int limit) {
        ArrayList<String> list = new ArrayList<>();

        while (list.size() < limit && list.size() < commandCount.size()) {
            int max = 0;
            String highest = "";
            //Get highest
            for (String c: commandCount.keySet()) {
                //Exclude previous
                if (!list.contains(c) && commandCount.get(c) > max) {
                    highest = c;
                    max = commandCount.get(c);
                }
            }

            list.add(highest);
        }

        return list;
    }

}

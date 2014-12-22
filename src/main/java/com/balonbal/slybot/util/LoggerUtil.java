package com.balonbal.slybot.util;

import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.lib.Reference;
import com.balonbal.slybot.lib.Settings;
import org.pircbotx.User;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.MessageEvent;

import java.io.*;
import java.util.Date;

public class LoggerUtil {

    public static void log(Event<SlyBot> event) {
        if (event instanceof MessageEvent) {
            String logPath = Reference.LOG_CHANNEL_FILE;

            //Get variables
            Date date = new Date();
            date.setTime(event.getTimestamp());
            User user = ((MessageEvent) event).getUser();
            String message = ((MessageEvent) event).getMessage();

            //Resolve actual path
            logPath = logPath.replaceAll("\\$NETWORK", Settings.network);
            logPath = logPath.replaceAll("\\$CHANNEL", ((MessageEvent) event).getChannel().getName().toLowerCase());

            //Check and create file
            File logFile = new File(logPath);
            if (!logFile.getParentFile().exists()) logFile.getParentFile().mkdirs();
            try {
                if (!logFile.exists()) logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Open a writer and write message to file
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)))) {
                writer.println(String.format("[%s] <%s> %s", date.toString(), user.getNick(), message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

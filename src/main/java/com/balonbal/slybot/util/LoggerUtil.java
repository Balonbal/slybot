package com.balonbal.slybot.util;

import com.balonbal.slybot.Main;
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
            //Get variables
            Date date = new Date();
            date.setTime(event.getTimestamp());
            User user = ((MessageEvent) event).getUser();
            String message = ((MessageEvent) event).getMessage();

            log(Settings.network, ((MessageEvent) event).getChannel().getName().toLowerCase(), date, user, message);
        }
    }

    public static void log(String network, String channel, Date date, User user, String message) {
        String logPath = Reference.LOG_CHANNEL_FILE;

        //Resolve actual path
        logPath = logPath.replaceAll("\\$NETWORK", network);
        logPath = logPath.replaceAll("\\$CHANNEL", channel);

        log(new File(logPath), date, user, message);
    }

    public static void log(File logFile, Date date, User user, String message) {
        //Check for or create file recursively
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

    public static int countLines(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }

}

package com.balonbal.slybot.commands;

import org.apache.commons.lang3.StringUtils;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;

public class CommandSay implements Command {


    @Override
    public String[] getTriggers() {
        return new String[] {
                "say",
                "echo"
        };
    }

    @Override
    public boolean requiresOP() {
        return false;
    }

    @Override
    public boolean channelCommand() {
        return true;
    }

    @Override
    public boolean pmCommand() {
        return true;
    }

    @Override
    public String[] help() {
        return new String[] {
                "Used to echo a string back, can only be used by channel operators or per PM",
                "Example: " + Colors.BOLD + "SAY I can talk to myself, yay"
        };
    }

    @Override
    public void run(User user, Channel channel, String[] params) {

        //Convert the array to a single string
        String message = StringUtils.join(params, " ");

        //For channels
        if (channel != null) {
            //Prevent users from sending bot commands this way by adding a space
            channel.send().message(Colors.NORMAL + message);
        } else {
            //Let users talk with them selves
            user.send().message(message);
        }
    }
}

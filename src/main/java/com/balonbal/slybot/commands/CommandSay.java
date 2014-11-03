package com.balonbal.slybot.commands;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.lib.Reference;
import org.apache.commons.lang3.StringUtils;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;
import org.pircbotx.hooks.Event;

public class CommandSay implements Command {


    @Override
    public String getTrigger() {
        return "say";
    }

    @Override
    public int requiresOP() {
        return Reference.REQUIRES_OP_NONE;
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
    public void run(String[] params, Event<SlyBot> event) {

        //Convert the array to a single string
        String message = StringUtils.join(params, " ").substring(params[0].length() + 1);

        Main.getBot().reply(event, Colors.NORMAL + message);
    }
}

package com.balonbal.slybot.commands;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.core.CommandHandler;
import com.balonbal.slybot.lib.Reference;
import org.apache.commons.lang3.StringUtils;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;
import org.pircbotx.hooks.Event;

import java.util.ArrayList;

public class CommandHelp implements Command {

    @Override
    public String[] help() {
        return new String[]{
                "Used to clearify the use of a command.",
                "SYNTAX: " + Colors.BOLD + "HELP [command]" + Colors.NORMAL,
                "Example: HELP RTD",
                "Running the command with no parameters will return a list of commands"
        };
    }

    @Override
    public String run(String[] params, Event<SlyBot> event) {
        String[] response = null;
        if (params.length == 1) {
            //Fetch all the commands
            ArrayList<Command> cmds = Main.getCommandListener().getCommandHandler().getCommands();

            String commands = "";
            for (Command c: cmds) {
                commands += (commands.equals("") ? "" : ", ") + c.getTrigger();
            }

            response = new String[]{
                    "Available commands: ",
                    commands,
                    "Total: " + Colors.BOLD + cmds.size() + Colors.NORMAL + " items."
            };
        } else {
            Command command = Main.getCommandListener().getCommandHandler().getCommand(params[1]);
            if (command != null) {
                //Fetch the help strings for the given command
                response = command.help();
            }
        }

        if (response == null) return "false";

        for (String s : response) {
            event.getBot().reply(event, s);
        }

        return "true";
    }

    @Override
    public String getTrigger() {
        return "help";
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

}

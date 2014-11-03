package com.balonbal.slybot.commands;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.core.CommandHandler;
import com.balonbal.slybot.lib.Reference;
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
    public void run(String[] params, Event<SlyBot> event) {
        String[] response = null;
        if (params.length == 0) {
            //Fetch all the commands
            ArrayList<Command> cmds = Main.getCommandListener().getCommandHandler().getCommands();

            response = new String[cmds.size() + 2];
            response[0] = "Available commands: ";

            for (int i = 0; i < cmds.size(); i++) {
                //Add the first trigger for each command
                response[i + 1] = cmds.get(i).getTrigger();
            }

            response[response.length - 1] = "Total: " + Colors.BOLD + cmds.size() + Colors.NORMAL + " items.";
        } else {
            Command command = Main.getCommandListener().getCommandHandler().getCommand(params[1]);
            if (command != null) {
                //Fetch the help strings for the given command
                response = command.help();
            }
        }

        if (response == null) return;

        for (String s : response) {
            event.getBot().reply(event, s);
        }
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

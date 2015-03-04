package com.balonbal.slybot.core;

import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.commands.Command;
import com.balonbal.slybot.lib.Reference;
import com.balonbal.slybot.lib.Settings;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Set;

public class CommandHandler {

    private ArrayList<Command> commands;

    public static boolean isChannelOP(User u, Channel c) {
        return c.getOps().contains(u);
    }

    public void addCommands() {
        commands = new ArrayList<Command>();
        Reflections r = new Reflections("");
        //Get the subtypes of the command class
        Set<Class<? extends Command>> classes = r.getSubTypesOf(Command.class);

        //loop through found classes and add them as commands
        for (Class<? extends Command> c : classes) {
            try {
                Command cmd = c.newInstance();
                System.out.println("Loaded command: " + c.getName());
                getCommands().add(cmd);
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public String processCommand(String command, Event<SlyBot> e) {

        Command cmd = getCommand(command.contains(" ") ? command.substring(0, command.indexOf(" ")) : command);
        System.out.println("Running command: " + command);

        //Verify that the command exists
        if (cmd == null) return "false";
        //Check that it is used in a valid place
        if (!(cmd.channelCommand() && e instanceof MessageEvent) && !(cmd.pmCommand() && e instanceof PrivateMessageEvent))
            return "false";
        //Check permissions
        if (!hasPermission(e, cmd.requiresOP())) {
            e.getBot().reply(e, (e instanceof MessageEvent ? ((MessageEvent) e).getUser().getNick() : ((PrivateMessageEvent) e).getUser().getNick()) + ": You do not have the required permissions to do that");
            return "false";
        }

        return cmd.run(command.split("\\s+"), e);

        //runAlias(user, channel, cmd, params);

    }

    public boolean hasPermission(Event<SlyBot> e, int level) {
        MessageEvent messageEvent = null;
        PrivateMessageEvent privateMessageEvent = null;

        //Convert to compatible type
        if (e instanceof MessageEvent) messageEvent = (MessageEvent) e;
        if (e instanceof PrivateMessageEvent) privateMessageEvent = (PrivateMessageEvent) e;

        //Check the permissions
        switch (level) {
            case Reference.REQUIRES_OP_NONE: return true;
            case Reference.REQUIRES_OP_BOT: return ((messageEvent != null) ? e.getBot().isBotOP(messageEvent.getUser()) : e.getBot().isBotOP(privateMessageEvent.getUser()));
            case Reference.REQUIRES_OP_CHANNEL: return (e instanceof MessageEvent && isChannelOP(messageEvent.getUser(), messageEvent.getChannel()));
            case Reference.REQUIRES_OP_ANY: return hasPermission(e, Reference.REQUIRES_OP_BOT) || hasPermission(e, Reference.REQUIRES_OP_CHANNEL);
            case Reference.REQUIRES_OP_BOTH: return hasPermission(e, Reference.REQUIRES_OP_BOT) && hasPermission(e, Reference.REQUIRES_OP_CHANNEL);
            default: return false;
        }
    }

	public Command getCommand(String cmd) {
        for (Command c : getCommands()) {
            if (cmd.equalsIgnoreCase(c.getTrigger())) return c;
		}
		
		return null;
	}

    public boolean isCommand(String cmd) {
        return (getCommand(cmd) != null || Settings.aliases.containsKey(cmd.toUpperCase()));
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }

}

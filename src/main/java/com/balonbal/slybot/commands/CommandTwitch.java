package com.balonbal.slybot.commands;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.lib.Reference;
import com.balonbal.slybot.util.sites.twitch.TwitchSubscription;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import java.util.ArrayList;

public class CommandTwitch implements Command {


    @Override
    public String getTrigger() {
        return "twitch";
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
                "Used to subscribe to twitch channels. This will enable automatic notifications whenever someone goes live.",
                "SYNTAX: " + Colors.BOLD + "TWITCH <add | subscribe | unsubscribe | update> <name> [URL | channel name]"
        };
    }

    @Override
    public String run(String[] parameters, Event<SlyBot> event) {
        if (parameters.length == 2) {
            if (parameters[1].equalsIgnoreCase("update")) {

                event.getBot().getTwitch().notifySubscribers();
                event.getBot().reply(event, "All channels updated.");
                return "";
            } else if (parameters[1].equalsIgnoreCase("unsubscribe")) {

                //Remove the feed by name
                TwitchSubscription subscription = event.getBot().getTwitch().getSubscription(parameters[2]);

                if (subscription == null) {
                    event.getBot().reply(event, "Unable to unsubscribe - no such feed.");
                    return "";
                }

                //Channel - Channel is subscribed
                if (event instanceof MessageEvent) {
                    //Check permissions
                    if (Main.getCommandListener().getCommandHandler().hasPermission(event, Reference.REQUIRES_OP_ANY)) {
                        if (subscription.isSubscribed(((MessageEvent) event).getChannel().getName())) {
                            subscription.unsubscribe(((MessageEvent) event).getChannel().getName());
                            event.getBot().reply(event, "You have removed " + ((MessageEvent) event).getChannel().getName() + " from the subscription list.");
                            return "";
                        } else {
                            event.getBot().reply(event, "This channel is not subscribed to that channel.");
                            return "";
                        }
                    } else {
                        event.getBot().reply(event, ((MessageEvent) event).getUser().getNick() + ": You do not have the required permissions to do that.");
                        return "";
                    }
                } else {
                    //PM - User is subscribed
                    if (subscription.isSubscribed(((PrivateMessageEvent) event).getUser().getNick())) {
                        subscription.unsubscribe(((PrivateMessageEvent) event).getUser().getNick());
                        event.getBot().reply(event, "You have unsubscribed.");
                        return "";
                    } else {
                        event.getBot().reply(event, "You are not subscribed to that channel.");
                    }
                }

                if (event.getBot().getRssManager().removeFeed(parameters[2])) {
                    event.getBot().reply(event, "Removed channel " + Colors.BOLD + parameters[2] + ".");
                } else {
                    event.getBot().reply(event, "Unable to remove channel - no such channel.");
                }
            } else if (parameters[1].equalsIgnoreCase("subscribe")) {
                String subscriber = (event instanceof MessageEvent ? ((MessageEvent) event).getChannel().getName() : ((PrivateMessageEvent) event).getUser().getNick());

                TwitchSubscription subscription = event.getBot().getTwitch().getSubscription(parameters[2]);
                if (subscription != null && !subscription.isSubscribed(subscriber)) {
                    subscription.subscribe(subscriber);
                    event.getBot().reply(event, "Now subscribed.");
                    return "";
                } else {
                    event.getBot().reply(event, "No such feed or aldready subscribed");
                    return "";
                }
            } else if (parameters[1].equalsIgnoreCase("list")) {
                try {
                    ArrayList<TwitchSubscription> subscriptions = event.getBot().getTwitch().getSubscriptions();

                    String reply = "";
                    String subscriber = (event instanceof MessageEvent ? ((MessageEvent) event).getChannel().getName() : ((PrivateMessageEvent) event).getUser().getNick());

                    for (TwitchSubscription subscription : subscriptions) {
                        reply += "[" + Colors.BOLD + (subscription.isSubscribed(subscriber) ? Colors.DARK_GREEN : Colors.OLIVE) + subscription.getName() + Colors.NORMAL + "] ";
                    }

                    event.getBot().reply(event, reply);
                    event.getBot().reply(event, "Total: " + Colors.BOLD + subscriptions.size() + Colors.NORMAL + " items.");
                    return "";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (parameters.length == 4 && parameters[1].equalsIgnoreCase("add")) {
            String subscriber = null;
            if (event instanceof PrivateMessageEvent) {
                subscriber = ((PrivateMessageEvent) event).getUser().getNick();
            } else {
                if (Main.getCommandListener().getCommandHandler().hasPermission(event, Reference.REQUIRES_OP_ANY)) {
                    subscriber = ((MessageEvent) event).getChannel().getName();
                } else {
                    event.getBot().reply(event, ((MessageEvent) event).getUser().getNick() + ": You do not have the required permissions to do that.");
                }
            }

            if (subscriber == null) {
                return "";
            }

            ArrayList<String> list = new ArrayList<>();
            list.add(subscriber);

            String channel = parameters[3];

            if (channel.matches("https?://twitch\\.tv/.*")) {
                channel = channel.substring(channel.lastIndexOf("/"));
            }

            event.getBot().getTwitch().addSubscription(channel, parameters[2], list);
            event.getBot().reply(event, "Channel added, " + subscriber + " has been subscribed automatically.");
            return "";
        }

        event.getBot().reply(event, "Error, BAD SYNTAX. " + Colors.BOLD + "TWITCH <add | subscribe | unsubscribe | update | list> <name> [url]");
        return "";
    }
}

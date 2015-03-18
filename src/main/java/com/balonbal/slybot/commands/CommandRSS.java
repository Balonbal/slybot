package com.balonbal.slybot.commands;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.lib.Reference;
import com.balonbal.slybot.util.rss.RSSSubscription;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class CommandRSS implements Command {
    @Override
    public String getTrigger() {
        return "RSS";
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
                "Used to subscribe to rss feeds.",
                "SYNTAX: " + Colors.BOLD + "RSS <add | subscribe | unsubscribe | update | list> <name> [url]",
                "Channel/bot OP required for channels. \"RSS UPDATE *\" will start a full update."
        };
    }

    @Override
    public String run(String[] parameters, Event<SlyBot> event) {
        if (parameters.length == 3) {
            if (parameters[1].equalsIgnoreCase("update")) {

                if (parameters[2].equals("*")) {
                    event.getBot().getRssManager().updateFeeds();
                    event.getBot().reply(event, "All feeds updated.");
                    return "";
                }

                //Get the feed from the ID/name
                RSSSubscription subscription = event.getBot().getRssManager().getFeed(parameters[2]);

                //If the feed  exists, update
                if (subscription != null) {
                    subscription.update();
                    event.getBot().reply(event, "Successfully updated feed.");
                    return "";
                } else {
                    event.getBot().reply(event, "Unable to update feed - no such feed.");
                    return "";
                }
            } else if (parameters[1].equalsIgnoreCase("unsubscribe")) {

                //Remove the feed by name
                RSSSubscription subscription = event.getBot().getRssManager().getFeed(parameters[2]);

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
                            event.getBot().reply(event, "This channel is not subscribed to that feed.");
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
                        event.getBot().reply(event, "You are not subscribed to that feed.");
                    }
                }

                if (event.getBot().getRssManager().removeFeed(parameters[2])) {
                    event.getBot().reply(event, "Removed feed " + Colors.BOLD + parameters[2] + ".");
                } else {
                    event.getBot().reply(event, "Unable to remove feed - no such feed.");
                }
            } else if (parameters[1].equalsIgnoreCase("subscribe")) {
                String subscriber = (event instanceof MessageEvent ? ((MessageEvent) event).getChannel().getName() : ((PrivateMessageEvent) event).getUser().getNick());

                RSSSubscription subscription = event.getBot().getRssManager().getFeed(parameters[2]);
                if (subscription != null && !subscription.isSubscribed(subscriber)) {
                    subscription.appendSetting("subscribers", subscriber);
                    event.getBot().reply(event, "Now subscribed.");
                    return "";
                } else {
                    event.getBot().reply(event, "No such feed or aldready subscribed");
                    return "";
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

            try {
                ArrayList<String> list = new ArrayList<>();
                list.add(subscriber);
                event.getBot().getRssManager().addFeed(new URL(parameters[3]), parameters[2], list);
                event.getBot().reply(event, "Feed added, " + subscriber + " has been subscribed automatically.");
                return "";
            } catch (MalformedURLException e) {
                event.getBot().reply(event, "Error, BAD URL.");
                return "";
            }
        } else if (parameters[1].equalsIgnoreCase("list")) {
            try {
                ArrayList<RSSSubscription> subscriptions = event.getBot().getRssManager().getFeeds();

                String reply = "";
                String subscriber = (event instanceof MessageEvent ? ((MessageEvent) event).getChannel().getName() : ((PrivateMessageEvent) event).getUser().getNick());

                if (parameters.length > 2) {
                    RSSSubscription subscription = event.getBot().getRssManager().getFeed(parameters[2]);
                    if (subscription == null) {
                        event.getBot().reply(event, "No such feed found");
                        return "";
                    }
                    event.getBot().reply(event, "[" + Colors.BOLD + (subscription.isSubscribed(subscriber) ? Colors.DARK_GREEN : Colors.OLIVE) + subscription.getID() + Colors.NORMAL + " - " + subscription.getURL() + "] ");
                    return "";
                }

                for (RSSSubscription subscription : subscriptions) {
                    reply += "[" + Colors.BOLD + (subscription.isSubscribed(subscriber) ? Colors.DARK_GREEN : Colors.OLIVE) + subscription.getID() + Colors.NORMAL + "] ";
                }

                event.getBot().reply(event, reply);
                event.getBot().reply(event, "Total: " + Colors.BOLD + subscriptions.size() + Colors.NORMAL + " items.");
                return "";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println(parameters.length);

        event.getBot().reply(event, "Error, BAD SYNTAX. " + Colors.BOLD + "RSS <add | subscribe | unsubscribe | update> <name> [url]");
        return "";
    }
}

package com.balonbal.slybot.listeners;

import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.util.LoggerUtil;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

public class LoggerListener extends ListenerAdapter<SlyBot> {

    @Override
    public void onMessage(MessageEvent<SlyBot> event) throws Exception {
        System.out.printf("%s -> %s: %s\n", event.getUser().getNick(), event.getChannel().getName(), event.getMessage());
        LoggerUtil.log(event);
    }

    @Override
    public void onPrivateMessage(PrivateMessageEvent<SlyBot> event) throws Exception {
        System.out.printf("%s ->[PM]-> me: %s\n", event.getUser().getNick(), event.getMessage());
    }
}

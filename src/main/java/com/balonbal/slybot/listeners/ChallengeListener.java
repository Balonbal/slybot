package com.balonbal.slybot.listeners;

import com.balonbal.slybot.Main;
import com.balonbal.slybot.SlyBot;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class ChallengeListener extends ListenerAdapter<SlyBot> {

    @Override
    public void onMessage(MessageEvent<SlyBot> event) throws Exception {
        Main.getChallengeManager().doNextTurn(event.getUser(), event.getMessage().split("\\s+"));
    }
}

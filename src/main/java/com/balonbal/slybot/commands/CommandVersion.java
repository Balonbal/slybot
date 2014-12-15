package com.balonbal.slybot.commands;

import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.lib.Reference;
import com.balonbal.slybot.util.VersionUtil;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;

public class CommandVersion implements Command {
    @Override
    public String getTrigger() {
        return "version";
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
                "Get the current version of the running slybot"
        };
    }

    @Override
    public String run(String[] parameters, Event<SlyBot> event) {
        //Get the local version and build
        String localVersion = event.getBot().getVersion();
        String localBuild = localVersion.substring(localVersion.lastIndexOf(" ") + 1);

        event.getBot().reply(event, "Currently running slybot version: " + Colors.BLUE + localVersion);

        //Get upstream build version
        String SHA =  VersionUtil.getRemoteSHA("Balonbal", "slybot");
        String remoteBuild = SHA.substring(0, localBuild.length());

        if (!localBuild.equals(remoteBuild)) {
            //Send a friendly reminder that a newer version exists
            event.getBot().reply(event, "Newer version " + Colors.RED + remoteBuild + Colors.NORMAL + " was found, consider updating.");
            event.getBot().reply(event, "Newest commit: " + Colors.DARK_BLUE + VersionUtil.getCommit("Balonbal", "slybot", SHA));
        }

        return localVersion;
    }
}

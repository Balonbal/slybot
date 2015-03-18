package com.balonbal.slybot.commands;

import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.lib.Reference;
import com.balonbal.slybot.lib.Settings;
import com.balonbal.slybot.util.sites.ColorUtil;
import com.balonbal.slybot.util.sites.mal.Anime;
import com.balonbal.slybot.util.sites.mal.MyAnimeList;
import org.apache.commons.lang3.StringUtils;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandMAL implements Command {


    @Override
    public String getTrigger() {
        return "MAL";
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
                "Used to search MAL, myanimelist, for anime. SYNTAX: " + Colors.BOLD + "MAL [-n <number>] <query>",
                "-n is used to determine how many results to display, defaults to one, max is 5 (if available)."
        };
    }

    @Override
    public String run(String[] parameters, Event<SlyBot> event) {
        if (!Settings.accounts.containsKey("mal")) {
            event.getBot().reply(event, "Error, not logged in to service, ask a bot operator to set up an account.");
        }

        int results = 1;
        int start = 1;

        if (parameters.length > 3 && parameters[1].equals("-n")) {
            try {
                results = Integer.parseInt(parameters[2]);

                if (results < 1) results = 1;
                if (results > 5) results = 5;

                start = 3;
            } catch (NumberFormatException e) {
                //Ignore, use default to one result
            }
        }

        ArrayList<Anime> result = new ArrayList<>();

        try {
            result = MyAnimeList.searchAnime(StringUtils.join(parameters, " ", start, parameters.length), Settings.accounts.get("mal").get("username"), Settings.accounts.get("mal").get("password"));
        } catch (Exception e) {
            event.getBot().reply(event, "Error, unable to get results for your query.");
        }

        int i = 0;

        for (Anime anime: result) {
            String reply = ColorUtil.parseColors(Reference.MAL_REPLY);

            Matcher m = Pattern.compile("\\$(" + StringUtils.join(Reference.MAL_ANIME_TAGS, "|") + "|URL)", Pattern.CASE_INSENSITIVE).matcher(reply);
            System.out.println(m.pattern().pattern());

            StringBuffer buffer = new StringBuffer();

            while (m.find()) {
                m.appendReplacement(buffer, Matcher.quoteReplacement(getReplacement(m.group().substring(1), anime)));
            }

            m.appendTail(buffer);

            String[] replies = buffer.toString().split("\\n");

            for (String r: replies) {
                //Short down long messages
                if (r.length() > event.getBot().getConfiguration().getMaxLineLength() - 75) {
                    r = r.substring(0, event.getBot().getConfiguration().getMaxLineLength() - 75) + "...";
                }

                event.getBot().reply(event, r);
            }

            i++;
            if (i == results) {
                break;
            }
        }

        return "";
    }

    private String getReplacement(String group, Anime anime) {

        switch (group.toLowerCase()) {
            case Reference.MAL_ANIME_NAME:
                return anime.getTitle();
            case Reference.MAL_ANIME_ID:
                return String.valueOf(anime.getId());
            case Reference.MAL_ANIME_ENGLISH_NAME:
                return anime.getEnglishTitle();
            case Reference.MAL_ANIME_END_DATE:
                return anime.getEndDate().toString();
            case Reference.MAL_ANIME_IMAGE:
                return anime.getImageUrl();
            case Reference.MAL_ANIME_START_DATE:
                return String.valueOf(anime.getStartDate());
            case Reference.MAL_ANIME_SYNOPSIS:
                return anime.getSynopsis().replaceAll("\\n", "");
            case Reference.MAL_ANIME_TYPE:
                return anime.getType();
            case Reference.MAL_ANIME_STATUS:
                return anime.getStatus();
            case Reference.MAL_ANIME_NUM_EPISODES:
                return String.valueOf(anime.getEpisodes());
            case Reference.MAL_ANIME_SYNONYMS:
                return StringUtils.join(anime.getSynonyms(), ", ");
            case Reference.MAL_ANIME_SCORE:
                return String.valueOf(anime.getScore());
            case "url":
                return "http://myanimelist.net/anime/" + anime.getId() + "/";
            default:
                return "";
        }
    }
}

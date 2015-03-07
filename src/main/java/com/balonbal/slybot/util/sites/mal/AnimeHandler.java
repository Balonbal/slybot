package com.balonbal.slybot.util.sites.mal;

import com.balonbal.slybot.lib.Reference;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AnimeHandler extends DefaultHandler {

    private Anime anime;
    private String temp;
    private ArrayList<Anime> animeList;

    AnimeHandler() {
        animeList = new ArrayList<>();
    }

    @Override
    public void characters(char[] buffer, int start, int length) {
        temp = new String(buffer, start, length);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        //Reset the temp string
        temp = "";

        if (qName.equals("entry")) {
            anime = new Anime();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {

        //For parsing dates
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            switch (qName) {
                case Reference.MAL_ANIME_ID:
                    anime.setId(Integer.parseInt(temp));
                    break;
                case Reference.MAL_ANIME_NAME:
                    anime.setTitle(temp);
                    break;
                case Reference.MAL_ANIME_ENGLISH_NAME:
                    anime.setEpisodes(Integer.parseInt(temp));
                    break;
                case Reference.MAL_ANIME_SYNONYMS:
                    anime.setSynonyms(temp.split(";"));
                    break;
                case Reference.MAL_ANIME_SCORE:
                    anime.setScore(Double.parseDouble(temp));
                    break;
                case Reference.MAL_ANIME_TYPE:
                    anime.setType(temp);
                    break;
                case Reference.MAL_ANIME_STATUS:
                    anime.setStatus(temp);
                    break;
                case Reference.MAL_ANIME_START_DATE:
                    anime.setStartDate(format.parse(temp));
                    break;
                case Reference.MAL_ANIME_END_DATE:
                    anime.setEndDate(format.parse(temp));
                    break;
                case Reference.MAL_ANIME_SYNOPSIS:
                    anime.setSynopsis(temp);
                    break;
                case Reference.MAL_ANIME_IMAGE:
                    anime.setImageUrl(temp);
                    break;
                case "entry":
                    //End of entry
                    animeList.add(anime);
                default:
                    //Do nothing
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            //Ignore
        }
    }

    public ArrayList<Anime> getList() {
        return animeList;
    }
}

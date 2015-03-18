package com.balonbal.slybot.util.sites;

import org.pircbotx.Colors;

public class ColorUtil {

    public static String parseColors(String text) {
        text = text.replaceAll("\\$BOLD", Colors.BOLD);
        text = text.replaceAll("\\$NORMAL", Colors.NORMAL);
        text = text.replaceAll("\\$UNDERLINE", Colors.UNDERLINE);
        text = text.replaceAll("\\$REVERSE", Colors.REVERSE);
        text = text.replaceAll("\\$WHITE", Colors.WHITE);
        text = text.replaceAll("\\$BLACK", Colors.BLACK);
        text = text.replaceAll("\\$DARK_BLUE", Colors.DARK_BLUE);
        text = text.replaceAll("\\$DARK_GREEN", Colors.DARK_GREEN);
        text = text.replaceAll("\\$RED", Colors.RED);
        text = text.replaceAll("\\$BROWN", Colors.BROWN);
        text = text.replaceAll("\\$PURPLE", Colors.PURPLE);
        text = text.replaceAll("\\$OLIVE", Colors.OLIVE);
        text = text.replaceAll("\\$YELLOW", Colors.YELLOW);
        text = text.replaceAll("\\$GREEN", Colors.GREEN);
        text = text.replaceAll("\\$TEAL", Colors.TEAL);
        text = text.replaceAll("\\$CYAN", Colors.CYAN);
        text = text.replaceAll("\\$BLUE", Colors.BLUE);
        text = text.replaceAll("\\$MAGENTA", Colors.MAGENTA);
        text = text.replaceAll("\\$DARK_GRAY", Colors.DARK_GRAY);
        text = text.replaceAll("\\$LIGHT_GRAY", Colors.LIGHT_GRAY);

        return text;
    }

}

package com.balonbal.slybot.commands;

import com.balonbal.slybot.SlyBot;
import com.balonbal.slybot.lib.Reference;
import com.balonbal.slybot.util.TimeDateUtil;
import org.apache.commons.lang3.StringUtils;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandDate implements Command {

    private static final Pattern parentPattern = Pattern.compile("(?<!\\\\)%(D|F|t|T)");
    private static final Pattern pattern = Pattern.compile("(?<!\\\\)%(A|B|d|h|H|m|M|p|S|W|y|Y)");

    @Override
    public String getTrigger() {
        return "date";
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
                "Get the current time relative to the bot settings",
                "SYNTAX: " + Colors.BOLD + "DATE [-TIMEZONE] [expression with %FilterA:%FilterB]",
                "Valid filters are %A (weekday), %B (month name), %d (date of month), %D (%d/%m/%y), %F (%d-%m-%y), %h (hour 24h), %H (hour 12h), %m (month), %M (minute), %p (AM/PM), %S (seconds), %t (%h:%M:%S), %T (%H:%M:%S%p)",
                "(cont.) %W (week number), %y (last to digits of %Y), %Y (year)"
        };
    }

    @Override
    public void run(String[] parameters, Event<SlyBot> event) {

        Calendar calendar = Calendar.getInstance();
        int skip = 1;

        if (parameters[1].matches("-\\w+")) {
            String tZone = parameters[1].substring(1);

            //Get the specified timezone
            TimeZone timeZone = TimeZone.getTimeZone(tZone);
            calendar = Calendar.getInstance(timeZone);
            skip = 2;
        }

        String[] newParams = new String[parameters.length - skip];
        System.arraycopy(parameters, skip, newParams, 0, newParams.length);
        String format = StringUtils.join(newParams, " ");

        //Replace patterns containing patterns first
        for (Pattern p: new Pattern[] { parentPattern, pattern }) {
            Matcher matcher = p.matcher(format);
            StringBuffer buffer = new StringBuffer();

            while (matcher.find()) {
                matcher.appendReplacement(buffer, getReplaceMent(matcher, calendar));
            }

            matcher.appendTail(buffer);

            format = buffer.toString();
        }

        event.getBot().reply(event, format);
    }

    private String getReplaceMent(Matcher matcher, Calendar calendar) {
        String s = matcher.group();

        if (s.equals("%A"))      return TimeDateUtil.getDayOfWeek(calendar);
        else if (s.equals("%B")) return TimeDateUtil.getMonthName(TimeDateUtil.getMonthOfYear(calendar));
        else if (s.equals("%d")) return String.valueOf(TimeDateUtil.getDate(calendar));
        else if (s.equals("%D")) return "%d/%m/%y";
        else if (s.equals("%F")) return "%d-%m-%y";
        else if (s.equals("%h")) return addZeroes(TimeDateUtil.getHour(true, calendar));
        else if (s.equals("%H")) return addZeroes(TimeDateUtil.getHour(false, calendar));
        else if (s.equals("%m")) return String.valueOf(TimeDateUtil.getMonthOfYear(calendar));
        else if (s.equals("%M")) return addZeroes(TimeDateUtil.getMinute(calendar));
        else if (s.equals("%p")) return (TimeDateUtil.isPM(calendar) ? "PM" : "AM");
        else if (s.equals("%S")) return addZeroes(TimeDateUtil.getSecond(calendar));
        else if (s.equals("%t")) return "%h:%M:%S";
        else if (s.equals("%T")) return "%H:%M:%S%p";
        else if (s.equals("%W")) return String.valueOf(TimeDateUtil.getWeek(calendar));
        else if (s.equals("%y")) return String.valueOf(TimeDateUtil.getYear(calendar)).substring(2);
        else if (s.equals("%Y")) return String.valueOf(TimeDateUtil.getYear(calendar));
        else return "";
    }

    private String addZeroes(int i) {
        return (i < 10 ? "0" : "") + String.valueOf(i);
    }
}

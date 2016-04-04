package com.balonbal.slybot.util;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeDateUtil {

    public static int getYear(Calendar calendar) {
        return calendar.get(Calendar.YEAR);
    }

    public static int getMonthOfYear(Calendar calendar) {
        return calendar.get(Calendar.MONTH);
    }

    public static String getMonthName(int month) {
        switch (month) {
            case Calendar.JANUARY: return "January";
            case Calendar.FEBRUARY: return "February";
            case Calendar.MARCH: return "March";
            case Calendar.APRIL: return "April";
            case Calendar.MAY: return "May";
            case Calendar.JUNE: return "June";
            case Calendar.JULY: return "July";
            case Calendar.AUGUST: return "August";
            case Calendar.SEPTEMBER: return "September";
            case Calendar.OCTOBER: return "October";
            case Calendar.NOVEMBER: return "November";
            case Calendar.DECEMBER: return "December";
            default: return "";
        }
    }

    public static int getWeek(Calendar calendar) {
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    public static int getDate(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static String getDayOfWeek(Calendar calendar) {
        return getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
    }

    public static String getDayOfWeek(int day) {

        //Translate the integer to the corresponding day
        switch (day) {
            case Calendar.MONDAY: return "Monday";
            case Calendar.TUESDAY: return "Tuesday";
            case Calendar.WEDNESDAY: return "Wednesday";
            case Calendar.THURSDAY: return "Thursday";
            case Calendar.FRIDAY: return "Friday";
            case Calendar.SATURDAY: return "Saturday";
            case Calendar.SUNDAY: return "Sunday";
            default: return "";
        }
    }

    public static int getHour(boolean twentyFour, Calendar calendar) {
        return calendar.get(twentyFour ? Calendar.HOUR_OF_DAY : Calendar.HOUR);
    }

    public static boolean isPM(Calendar calendar) {
        return calendar.get(Calendar.AM_PM) == Calendar.PM;
    }

    public static int getMinute(Calendar calendar) {
        return calendar.get(Calendar.MINUTE);
    }

    public static int getSecond(Calendar calendar) {
        return calendar.get(Calendar.SECOND);
    }

    public static String stringify(long mills, String format) {
        Pattern pattern = Pattern.compile("\\$(d|h|m|s|M)");

        int days = (int) Math.floor(mills / (24 * 60 * 60 * 1000));
        mills %= 24*60*60*1000;
        int hours = (int) Math.floor(mills / (60 * 60 * 1000));
        mills %= 60*60*1000;
        int minutes = (int) Math.floor(mills / (60 * 1000));
        mills %= 60*1000;
        int secs = (int) Math.floor(mills / 1000);

        Matcher m = pattern.matcher(format);
        StringBuffer buffer = new StringBuffer();
        while (m.find()) {
            int r = 0;
            switch (m.group().substring(1)) {
                case "d":
                    r = days;
                    break;
                case "h":
                    r = hours;
                    break;
                case "m":
                    r = minutes;
                    break;
                case "s":
                    r = secs;
                    break;
                case "M":
                    r = (int) (mills % 1000);
                    break;
            }

            m.appendReplacement(buffer, ((r < 10 && !m.group().substring(1).equals("d")) ? "0" : "") + r);
        }

        m.appendTail(buffer);
        return buffer.toString();
    }
}

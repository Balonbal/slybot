package com.balonbal.slybot.util;

import java.util.Calendar;

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
}

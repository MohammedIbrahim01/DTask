package com.example.abdelazim.globaltask.utils;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class AppFormatter {


    public static String formatTime(int hourOfDay, int minute) {

        String AM_PM = " am";
        String formattedMinute = "";

        // Starting from 12 follow it by pm
        if (hourOfDay >= 12)
            AM_PM = " pm";
        // Starting from 13 decrease by 12
        if (hourOfDay >= 13)
            hourOfDay -= 12;
        // Format minute 3 to be 03
        if (minute < 10)
            formattedMinute = "0" + minute;
        else
            formattedMinute = String.valueOf(minute);

        return hourOfDay + ":" + formattedMinute + " " + AM_PM;
    }

    public static String formatTime(long time) {

        // Create calendar from this time
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        // Get hour and minute
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String AM_PM = " am";
        String formattedMinute = "";

        // Starting from 12 follow it by pm
        if (hourOfDay >= 12)
            AM_PM = " pm";
        // Starting from 13 decrease by 12
        if (hourOfDay >= 13)
            hourOfDay -= 12;
        // Format minute 3 to be 03
        if (minute < 10)
            formattedMinute = "0" + minute;
        else
            formattedMinute = String.valueOf(minute);

        return hourOfDay + ":" + formattedMinute + " " + AM_PM;
    }

    public static String formatDate(long time) {

        // Create calendar from this time
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        // Get months Symbols
        DateFormatSymbols symbols = new DateFormatSymbols();
        String[] months = symbols.getMonths();
        // Get day info from calendar
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);

        return day + "  " + months[month];
    }
}

package com.example.abdelazim.globaltask.utils;

import android.support.annotation.NonNull;

import java.util.Calendar;

public class AppTime {

    /**
     * Build calendar from an hour and set the minute and second to 0
     *
     * @param hourOfDay the exact hour
     * @return calendar with the exact hour, 0 minute and 0 second
     */
    @NonNull
    public static Calendar getExactCalendar(int hourOfDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }


    @NonNull
    public static Calendar getCalendar(int hourOfDay, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        return calendar;
    }
}

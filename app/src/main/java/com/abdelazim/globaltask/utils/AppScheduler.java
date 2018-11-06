package com.abdelazim.globaltask.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class AppScheduler {


    Context context;
    AlarmManager alarmManager;

    public AppScheduler(Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }


    /**
     * Fire receiver at the end of every day
     *
     * To clean the late tasks
     */
    public void scheduleTasksCleaner() {

        Calendar calendar = AppTime.getCalendar(23, 0, 0);
        Intent intent = new Intent(context, TasksCleaner.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, AppConstants.RC_TASKS_CLEANER_PI, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }


    /**
     * Fire receiver at the wakeup time
     *
     * To notify the user to set day tasks
     *
     * @param wakeupTime
     */
    public void scheduleWakeupNotification(long wakeupTime) {

        Intent intent = new Intent(context, NotificationPublisher.class);
        intent.putExtra(AppConstants.NOTIFICATION_PUBLISHER_ACTION, AppConstants.WAKEUP_NOTIFICATION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, AppConstants.RC_WAKEUP_NOTIFICATION_PI, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC, wakeupTime, AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}

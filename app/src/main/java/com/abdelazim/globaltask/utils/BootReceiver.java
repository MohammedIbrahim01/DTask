package com.abdelazim.globaltask.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

public class BootReceiver extends BroadcastReceiver {

    public static final String KEY_WAKEUP_TIME = "wakeup-time";
    public static final String DTASK_SHARED = "DTask-shared";

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(DTASK_SHARED, Context.MODE_PRIVATE);

        AppScheduler appScheduler = new AppScheduler(context);
        appScheduler.scheduleTasksCleaner();
        appScheduler.scheduleWakeupNotification(sharedPreferences.getLong(KEY_WAKEUP_TIME, Calendar.getInstance().getTimeInMillis()));
    }
}

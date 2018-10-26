package com.example.abdelazim.globaltask.utils;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationPublisher extends BroadcastReceiver {

    private static final String KEY_NOTIFICATION = "key-notification";
    private static final String KEY_NOTIFICATION_ID = "key-notification-id";

    @Override
    public void onReceive(Context context, Intent intent) {

        Notification notification = intent.getParcelableExtra(KEY_NOTIFICATION);
        int id = intent.getIntExtra(KEY_NOTIFICATION_ID, 0);

        AppNotifications appNotifications = new AppNotifications(context);

        appNotifications.notify(notification, id);
    }
}

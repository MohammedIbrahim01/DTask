package com.example.abdelazim.globaltask.utils;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.abdelazim.globaltask.repository.AppRepository;
import com.example.abdelazim.globaltask.repository.model.Task;

public class NotificationPublisher extends BroadcastReceiver {

    private static final String KEY_NOTIFICATION = "key-notification";
    private static final String KEY_TASK_ID = "key-task-id";

    private AppNotifications appNotifications;

    @Override
    public void onReceive(Context context, Intent intent) {

        appNotifications = new AppNotifications(context);

        if (intent.hasExtra(AppConstants.NOTIFICATION_PUBLISHER_ACTION)) {

            String action = intent.getStringExtra(AppConstants.NOTIFICATION_PUBLISHER_ACTION);
            if (action.equals(AppConstants.WAKEUP_NOTIFICATION)) {
                Log.i("WWW", "wakeup notification");
                appNotifications.notifyWakeup();

            } else if (action.equals(AppConstants.TASK_TIME_NOTIFICATION)) {
                Log.i("WWW", "task time notification");
                Notification notification = intent.getParcelableExtra(KEY_NOTIFICATION);
                int taskId = intent.getIntExtra(KEY_TASK_ID, 1000);

                // Mark the task as late
                AppRepository repository = new AppRepository(context);
                repository.markAsLate(taskId);


                appNotifications.notify(notification, taskId);
            }
        }
    }
}

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

    @Override
    public void onReceive(Context context, Intent intent) {

        Notification notification = intent.getParcelableExtra(KEY_NOTIFICATION);
        int taskId = intent.getIntExtra(KEY_TASK_ID, 1000);

        // Mark the task as late
        AppRepository repository = new AppRepository(context);
        repository.markAsLate(taskId);

        AppNotifications appNotifications = new AppNotifications(context);

        appNotifications.notify(notification, taskId);
    }
}

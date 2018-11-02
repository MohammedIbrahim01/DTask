package com.example.abdelazim.globaltask.utils;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.abdelazim.globaltask.R;
import com.example.abdelazim.globaltask.main.MainActivity;
import com.example.abdelazim.globaltask.repository.model.Task;

public class AppNotifications {


    private Context mContext;
    private AlarmManager alarmManager;
    private NotificationManager notificationManager;

    /**
     * Constructor
     *
     * Get mContext and alarmManager
     *
     * @param context
     */
    public AppNotifications(Context context) {

        mContext = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(AppConstants.CHANNEL_ID, AppConstants.CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
    }


    /**
     * Schedule the new task in its time
     *
     * @param task
     */
    public void scheduleNewTask(Task task) {

        // Create intent and put notification and notification id as extra values
        Intent intent = new Intent(mContext, NotificationPublisher.class);
        intent.putExtra(AppConstants.NOTIFICATION_PUBLISHER_ACTION, AppConstants.TASK_TIME_NOTIFICATION);
        intent.putExtra(AppConstants.KEY_NOTIFICATION, getNotification(task));
        intent.putExtra(AppConstants.KEY_TASK_ID, task.getId());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, (int) task.getTime() % 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC, task.getTime(), pendingIntent);
        } else
            alarmManager.set(AlarmManager.RTC, task.getTime(), pendingIntent);
    }

    public void removeScheduledTask(Task task) {

        // Create intent and put notification and notification id as extra values
        Intent intent = new Intent(mContext, NotificationPublisher.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, (int) task.getTime() % 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }

    public void notify(Notification notification, int id) {

        notificationManager.notify(id, notification);
    }

    private Notification getNotification(Task task) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, AppConstants.CHANNEL_ID)
                .setContentTitle(task.getTitle())
                .setContentText(task.getDescription())
                .setSmallIcon(R.drawable.notification_small)
                .setLargeIcon(getLargeIcon())
                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                .setLights(ContextCompat.getColor(mContext, R.color.colorAccent), 500, 2000)
                .setContentIntent(PendingIntent.getActivity(mContext, task.getId(), new Intent(mContext, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT))
                .setAutoCancel(true);

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        return builder.build();
    }

    private Bitmap getLargeIcon() {

        return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_dtask);
    }

    private Notification getWakeupNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, AppConstants.CHANNEL_ID)
                .setContentTitle("Time to set some Tasks")
                .setContentText("take a few moment to fill tasks list will make your day more organized")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("take a few moment to fill tasks list will make your day more organized"))
                .setSmallIcon(R.drawable.notification_small)
                .setLargeIcon(getLargeIcon())
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(PendingIntent.getActivity(mContext, AppConstants.RC_WAKEUP_NOTIFICATION_PI, new Intent(mContext, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT))
                .setAutoCancel(true);

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
            builder.setPriority(NotificationCompat.PRIORITY_LOW);
        }

        return builder.build();
    }

    public void notifyWakeup() {

        notificationManager.notify(22, getWakeupNotification());
    }
}

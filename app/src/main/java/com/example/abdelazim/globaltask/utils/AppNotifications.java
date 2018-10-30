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

    private static final String KEY_NOTIFICATION = "key-notification";
    private static final String KEY_TASK_ID = "key-task-id";

    private static final String CHANNEL_NAME = "global-task";
    private static final String CHANNEL_ID = "global-task-channel-id";

    private Context mContext;
    private AlarmManager alarmManager;


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
    }


    /**
     * Schedule the new task in its time
     *
     * @param task
     */
    public void scheduleNewTask(Task task) {

        // Create intent and put notification and notification id as extra values
        Intent intent = new Intent(mContext, NotificationPublisher.class);
        intent.putExtra(KEY_NOTIFICATION, getNotification(task));
        intent.putExtra(KEY_TASK_ID, task.getId());

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

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(id, notification);
    }

    private Notification getNotification(Task task) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setContentTitle(task.getTitle())
                .setContentText(task.getDescription())
                .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(task.getDescription()))
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getLargeImage()).bigLargeIcon(null))
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

    private Bitmap getLargeImage() {

        return BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_notifiction_image);
    }
}

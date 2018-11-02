package com.example.abdelazim.globaltask.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.abdelazim.globaltask.repository.local.AchievementsDao;
import com.example.abdelazim.globaltask.repository.local.AppDatabase;
import com.example.abdelazim.globaltask.repository.local.DayDao;
import com.example.abdelazim.globaltask.repository.local.DayWithAchievementsDao;
import com.example.abdelazim.globaltask.repository.local.TaskDao;
import com.example.abdelazim.globaltask.repository.model.Achievement;
import com.example.abdelazim.globaltask.repository.model.Day;
import com.example.abdelazim.globaltask.repository.model.Task;

import java.util.Calendar;
import java.util.List;

public class TasksCleaner extends BroadcastReceiver {

    List<Task> lateTaskList;
    TaskDao taskDao;
    AchievementsDao achievementsDao;
    DayDao dayDao;
    List<Day> allDayList;
    boolean fetched;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("WWW", "tasks cleaner");
        AppDatabase database = AppDatabase.getInstance(context);
        taskDao = database.taskDao();
        achievementsDao = database.achievementsDao();
        dayDao = database.dayDao();

        AppExecutors.getInstance().diskIO.execute(new Runnable() {
            @Override
            public void run() {
                fetched = false;
                lateTaskList = taskDao.getTasks();
                allDayList = dayDao.getAllDayList();
                fetched = true;
            }
        });

        while (!fetched) ;
        if (lateTaskList == null || lateTaskList.size() == 0)
            return;

        int lastDayIndex = allDayList.size() - 1;
        final Day day = allDayList.get(lastDayIndex);
        day.setLateNum(lateTaskList.size());

        AppExecutors.getInstance().diskIO.execute(new Runnable() {
            @Override
            public void run() {
                dayDao.insertDay(day);
            }
        });

        final Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < lateTaskList.size(); i++) {
            final Task task = lateTaskList.get(i);

            calendar.setTimeInMillis(task.getTime());

            AppExecutors.getInstance().diskIO.execute(new Runnable() {
                @Override
                public void run() {

                    Achievement achievement = new Achievement(task.getTitle(), task.getDescription(), calendar.get(Calendar.DAY_OF_YEAR), task.getTime());
                    achievement.setLate(true);
                    achievementsDao.insertAchievement(achievement);
                    taskDao.deleteTask(task);
                }
            });
        }

    }
}

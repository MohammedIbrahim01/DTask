package com.abdelazim.globaltask.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.abdelazim.globaltask.repository.local.AchievementsDao;
import com.abdelazim.globaltask.repository.local.AppDatabase;
import com.abdelazim.globaltask.repository.local.DayDao;
import com.abdelazim.globaltask.repository.local.DayWithAchievementsDao;
import com.abdelazim.globaltask.repository.local.TaskDao;
import com.abdelazim.globaltask.repository.model.Achievement;
import com.abdelazim.globaltask.repository.model.Day;
import com.abdelazim.globaltask.repository.model.DayWithAchievements;
import com.abdelazim.globaltask.repository.model.Task;
import com.abdelazim.globaltask.repository.remote.RemoteDatabase;
import com.abdelazim.globaltask.utils.AppExecutors;
import com.abdelazim.globaltask.utils.AppFormatter;
import com.abdelazim.globaltask.utils.AppNotifications;

import java.util.Calendar;
import java.util.List;

public class AppRepository {

    // Database and executors
    private AppDatabase database;
    private AppExecutors executors;
    // AppNotification
    private AppNotifications appNotifications;
    // Data access objects
    private TaskDao taskDao;
    private AchievementsDao achievementDao;
    private DayDao dayDao;
    private DayWithAchievementsDao dayWithAchievementsDao;

    private LiveData<List<Task>> taskList;
    private LiveData<List<DayWithAchievements>> dayWithAchievementsList;
    // Singleton pattern variables
    private static final Object LOCK = new Object();
    private static AppRepository sInstance;

    //
    private RemoteDatabase remoteDatabase;

    /**
     * Constructor
     *
     * Instantiate AppDatabase, AppExecutors and dao-s
     *
     * @param applicationContext to get instance of AppDatabase
     */
    public AppRepository(Context applicationContext) {


        database = AppDatabase.getInstance(applicationContext);
        executors = AppExecutors.getInstance();

        appNotifications = new AppNotifications(applicationContext);

        taskDao = database.taskDao();
        achievementDao = database.achievementsDao();
        dayDao = database.dayDao();
        dayWithAchievementsDao = database.dayWithAchievementsDao();

        remoteDatabase = new RemoteDatabase();
    }

    /**
     * Get Instance method
     *
     * Singleton pattern
     *
     * @param applicationContext to pass to constructor
     * @return single instance of AppRepository
     */
    public static AppRepository getInstance(Context applicationContext) {
        if (sInstance == null) {
            synchronized (LOCK) {

                sInstance = new AppRepository(applicationContext);
            }
        }

        return sInstance;
    }


    /**
     * Save new task to database
     *
     * @param task to insert in the database
     */
    public void saveNewTask(final Task task) {

        executors.diskIO.execute(new Runnable() {
            @Override
            public void run() {
                long id = database.taskDao().insertTask(task);

                Task justAddedTask = database.taskDao().getTaskById((int) id);

                appNotifications.scheduleNewTask(justAddedTask);
            }
        });
    }


    /**
     * Get task list as LiveData
     */
    private volatile boolean taskListFetched;

    public LiveData<List<Task>> getTaskList() {

        taskListFetched = false;

        executors.diskIO.execute(new Runnable() {
            @Override
            public void run() {
                taskList = taskDao.getTasksLD();
                taskListFetched = true;
            }
        });
        while (!taskListFetched) ;
        return taskList;
    }


    public void finishTask(final Task task) {

        // Remove the task from alarmManager if still exists in case task is finished before its time
        appNotifications.removeScheduledTask(task);

        final int dayOfYear;

        // Get day
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(task.getTime());
        dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

        // Create day
        final Day day = new Day(dayOfYear, AppFormatter.formatDate(task.getTime()));
        // Create achievement
        final Achievement achievement = new Achievement(task.getTitle(), task.getDescription(), dayOfYear, task.getTime());


        /**
         * Delete the task
         *
         * Insert Day and achievement
         */

        executors.diskIO.execute(new Runnable() {
            @Override
            public void run() {

                // Delete from task table
                taskDao.deleteTask(task);

                // Insert new achievement
                achievementDao.insertAchievement(achievement);

                // Get all days and check if that day exists or need to be added
                List<Day> dayList = dayDao.getAllDayList();

                for (int i = 0; i < dayList.size(); i++) {

                    if (dayList.get(i).getDay() == dayOfYear)
                        return;
                }

                dayDao.insertDay(day);
            }
        });
    }

    /**
     * Get DayWithAchievement as LiveData
     */

    private volatile boolean dayWithAchievementFetched;

    public LiveData<List<DayWithAchievements>> getDayWithAchievementsList() {

        dayWithAchievementFetched = false;

        executors.diskIO.execute(new Runnable() {
            @Override
            public void run() {

                dayWithAchievementsList = dayWithAchievementsDao.getAllDayWithAchievements();
                dayWithAchievementFetched = true;
            }
        });
        while (!dayWithAchievementFetched) ;
        return dayWithAchievementsList;
    }


    public void markAsLate(final int id) {

        executors.diskIO.execute(new Runnable() {
            @Override
            public void run() {

                Task task = taskDao.getTaskById(id);
                task.setLate(true);
                taskDao.updateTask(task);
            }
        });
    }

    public void saveEditedTask(final Task task) {

        executors.diskIO.execute(new Runnable() {
            @Override
            public void run() {

                long id = database.taskDao().insertTask(task);

                Task justAddedTask = database.taskDao().getTaskById((int) id);

                appNotifications.scheduleNewTask(justAddedTask);
            }
        });
    }

    public void delete(final Task task) {

        executors.diskIO.execute(new Runnable() {
            @Override
            public void run() {

                taskDao.deleteTask(task);
                appNotifications.removeScheduledTask(task);
            }
        });
    }

    public void publishTask(Task task) {

        remoteDatabase.publishTask(task);
    }
}

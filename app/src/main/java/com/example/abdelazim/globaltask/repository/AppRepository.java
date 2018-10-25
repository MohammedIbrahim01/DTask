package com.example.abdelazim.globaltask.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.util.Log;

import com.example.abdelazim.globaltask.repository.local.AchievementsDao;
import com.example.abdelazim.globaltask.repository.local.AppDatabase;
import com.example.abdelazim.globaltask.repository.local.DayDao;
import com.example.abdelazim.globaltask.repository.local.DayWithAchievementsDao;
import com.example.abdelazim.globaltask.repository.local.TaskDao;
import com.example.abdelazim.globaltask.repository.model.Achievement;
import com.example.abdelazim.globaltask.repository.model.Day;
import com.example.abdelazim.globaltask.repository.model.DayWithAchievements;
import com.example.abdelazim.globaltask.repository.model.Task;
import com.example.abdelazim.globaltask.utils.AppExecutors;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AppRepository {

    // Database and executors
    private AppDatabase database;
    private AppExecutors executors;
    // Data access objects
    private TaskDao taskDao;
    private AchievementsDao achievementDao;
    private DayDao dayDao;
    private DayWithAchievementsDao dayWithAchievementsDao;

    private LiveData<List<Task>> taskList;
    private LiveData<List<Task>> achievementList;
    private LiveData<List<DayWithAchievements>> dayWithAchievementsList;
    // Singleton pattern variables
    private static final Object LOCK = new Object();
    private static AppRepository sInstance;

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

        taskDao = database.taskDao();
        achievementDao = database.achievementsDao();
        dayDao = database.dayDao();
        dayWithAchievementsDao = database.dayWithAchievementsDao();
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
                database.taskDao().insertTask(task);
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
                taskList = taskDao.getTasks();
                taskListFetched = true;
            }
        });
        while (!taskListFetched) ;
        return taskList;
    }


    public void finishTask(final Task task) {

        final int dayOfYear;

        // Get day
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(task.getTime());
        dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

        // Create day
        String header = calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.MONTH);
        final Day day = new Day(dayOfYear, header);
        // Create achievement
        final Achievement achievement = new Achievement(task.getTitle(), task.getDescription(), dayOfYear);


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
                Log.d("WWW", "insert day");
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
}

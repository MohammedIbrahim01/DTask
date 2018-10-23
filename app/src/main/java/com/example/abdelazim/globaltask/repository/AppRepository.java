package com.example.abdelazim.globaltask.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.example.abdelazim.globaltask.repository.local.AppDatabase;
import com.example.abdelazim.globaltask.repository.local.TaskDao;
import com.example.abdelazim.globaltask.repository.model.Task;
import com.example.abdelazim.globaltask.utils.AppExecutors;

import java.util.List;

public class AppRepository {

    private AppDatabase database;
    private TaskDao taskDao;
    private AppExecutors executors;
    private LiveData<List<Task>> taskList;
    private LiveData<List<Task>> achievementList;

    private static final Object LOCK = new Object();
    private static AppRepository sInstance;

    public AppRepository(Context applicationContext) {

        database = AppDatabase.getInstance(applicationContext);
        taskDao = database.taskDao();
        executors = AppExecutors.getInstance();
    }


    public static AppRepository getInstance(Context applicationContext) {
        if (sInstance == null) {
            synchronized (LOCK) {

                sInstance = new AppRepository(applicationContext);
            }
        }

        return sInstance;
    }


    public void saveTask(final Task task) {

        executors.diskIO.execute(new Runnable() {
            @Override
            public void run() {
                database.taskDao().insertTask(task);
            }
        });
    }


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


    private volatile boolean achievementListFetched;

    public LiveData<List<Task>> getAchievements() {

        achievementListFetched = false;

        executors.diskIO.execute(new Runnable() {
            @Override
            public void run() {

                achievementList = taskDao.getAchievements();
                achievementListFetched = true;
            }
        });
        while (!achievementListFetched) ;
        return achievementList;
    }

    public void markAsDone(final Task task) {

        task.setDone(true);

        executors.diskIO.execute(new Runnable() {
            @Override
            public void run() {

                taskDao.updateTask(task);
            }
        });
    }
}

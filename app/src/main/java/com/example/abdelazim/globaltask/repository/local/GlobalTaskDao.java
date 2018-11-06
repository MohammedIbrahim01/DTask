package com.example.abdelazim.globaltask.repository.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.abdelazim.globaltask.repository.model.GlobalTask;
import com.example.abdelazim.globaltask.repository.model.Task;

import java.util.List;

@Dao
public interface GlobalTaskDao {

    @Query("SELECT * FROM task_table ORDER BY time")
    List<GlobalTask> getGlobalTasks();

    @Query("SELECT * FROM task_table ORDER BY time")
    LiveData<List<GlobalTask>> getGlobalTasksLD();

    @Insert
    long insertTask(GlobalTask task);

    @Delete
    void deleteTask(GlobalTask task);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(GlobalTask task);
}


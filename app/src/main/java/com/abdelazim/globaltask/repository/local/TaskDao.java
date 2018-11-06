package com.abdelazim.globaltask.repository.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.abdelazim.globaltask.repository.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM task_table ORDER BY time")
    LiveData<List<Task>> getTasksLD();

    @Query("SELECT * FROM task_table ORDER BY time")
    List<Task> getTasks();

    @Query("SELECT * FROM task_table WHERE id = :id")
    Task getTaskById(int id);

    @Query("SELECT * FROM task_table WHERE id = :id")
    LiveData<Task> getTaskByIdLD(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTask(Task task);

    @Delete
    void deleteTask(Task task);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(Task task);
}

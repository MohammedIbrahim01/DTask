package com.example.abdelazim.globaltask.repository.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.abdelazim.globaltask.repository.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM task_table WHERE done = 0 ORDER BY time")
    LiveData<List<Task>> getTasks();

    @Query("SELECT * FROM task_table WHERE done = 1 ORDER BY time")
    LiveData<List<Task>> getAchievements();

    @Query("SELECT * FROM task_table WHERE id = :id")
    LiveData<Task> getTaskById(int id);

    @Query("SELECT * FROM task_table WHERE id = :id AND done LIKE 0")
    LiveData<Task> getAchievementById(int id);

    @Insert
    void insertTask(Task task);

    @Delete
    void deleteTask(Task task);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(Task task);
}

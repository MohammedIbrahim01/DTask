package com.example.abdelazim.globaltask.repository.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.example.abdelazim.globaltask.repository.model.DayWithAchievements;

import java.util.List;

@Dao
public interface DayWithAchievementsDao {

    @Query("SELECT * FROM day")
    @Transaction
    LiveData<List<DayWithAchievements>> getAllDayWithAchievements();
}

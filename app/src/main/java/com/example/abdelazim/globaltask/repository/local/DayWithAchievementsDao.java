package com.example.abdelazim.globaltask.repository.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.abdelazim.globaltask.repository.model.DayWithAchievements;

import java.util.List;

@Dao
public interface DayWithAchievementsDao {

    @Query("SELECT * FROM day")
    LiveData<List<DayWithAchievements>> getAllDayWithAchievements();
}

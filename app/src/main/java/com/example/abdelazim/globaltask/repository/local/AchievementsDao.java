package com.example.abdelazim.globaltask.repository.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.abdelazim.globaltask.repository.model.Achievement;

import java.util.List;

@Dao
public interface AchievementsDao {

    @Query("SELECT * FROM Achievement ORDER BY time")
    LiveData<List<Achievement>> getAllAchievements();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAchievement(Achievement achievement);
}

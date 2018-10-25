package com.example.abdelazim.globaltask.repository.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.abdelazim.globaltask.repository.model.Day;

import java.util.List;

@Dao
public interface DayDao {

    @Query("SELECT * FROM day")
    LiveData<List<Day>> getAllDayListLD();

    @Query("SELECT * FROM day")
    List<Day> getAllDayList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDay(Day day);
}

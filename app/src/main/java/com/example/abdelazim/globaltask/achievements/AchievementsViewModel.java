package com.example.abdelazim.globaltask.achievements;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.example.abdelazim.globaltask.repository.AppRepository;
import com.example.abdelazim.globaltask.repository.model.Achievement;
import com.example.abdelazim.globaltask.repository.model.DayWithAchievements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AchievementsViewModel extends ViewModel {

    private AppRepository repository;
    private AchievementAdapter adapter;


    /**
     * Set the AppRepository and taskAdapter
     *
     * @param repository AppRepository
     * @param adapter    TaskAdapter
     */
    public void start(AppRepository repository, AchievementAdapter adapter) {

        this.repository = repository;
        this.adapter = adapter;
    }


    /**
     * Observe DayWithAchievementsList changes
     *
     * @param owner LifecycleOwner
     */
    void observe(LifecycleOwner owner) {

        repository.getDayWithAchievementsList().observe(owner, new Observer<List<DayWithAchievements>>() {
            @Override
            public void onChanged(@Nullable List<DayWithAchievements> dayWithAchievements) {

                if (dayWithAchievements == null)
                    return;

                displayAchievements(dayWithAchievements);
            }
        });
    }


    /**
     * Pass dayWithAchievementsList to the adapter
     *
     * Then notifyAdapter
     *
     * @param dayWithAchievementsList
     */
    private void displayAchievements(List<DayWithAchievements> dayWithAchievementsList) {

        adapter.setDayWithAchievementsList(dayWithAchievementsList);
        adapter.notifyDataSetChanged();
    }
}

package com.example.abdelazim.globaltask.achievements;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.abdelazim.globaltask.repository.AppRepository;
import com.example.abdelazim.globaltask.repository.model.DayWithAchievements;

import java.util.List;

public class AchievementsViewModel extends ViewModel {

    private AppRepository repository;
    private AchievementAdapter adapter;
    private AchievementView view;


    /**
     * Set the AppRepository and taskAdapter
     *
     * @param repository AppRepository
     * @param adapter    TaskAdapter
     */
    public void start(AppRepository repository, AchievementAdapter adapter, AchievementView view) {

        this.repository = repository;
        this.adapter = adapter;
        this.view = view;
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

                if (dayWithAchievements == null || dayWithAchievements.size() == 0) {
                    view.displayNoAchievementsView();
                } else {
                    view.displayAchievementsView();
                    displayAchievements(dayWithAchievements);
                }

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

    public interface AchievementView {

        void displayNoAchievementsView();

        void displayAchievementsView();
    }
}

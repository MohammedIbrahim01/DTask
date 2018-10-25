package com.example.abdelazim.globaltask.achievements;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.example.abdelazim.globaltask.repository.AppRepository;
import com.example.abdelazim.globaltask.repository.model.Achievement;
import com.example.abdelazim.globaltask.repository.model.Day;
import com.example.abdelazim.globaltask.repository.model.DayWithAchievements;
import com.example.abdelazim.globaltask.repository.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AchievementsViewModel extends ViewModel {

    private AppRepository repository;
    private AchievementAdapter adapter;

    public void start(AppRepository repository, AchievementAdapter adapter) {

        this.repository = repository;
        this.adapter = adapter;
    }


    public void observe(LifecycleOwner owner) {

        getDayWithAchievements().observe(owner, new Observer<List<DayWithAchievements>>() {
            @Override
            public void onChanged(@Nullable List<DayWithAchievements> dayWithAchievements) {

                if (dayWithAchievements == null)
                    return;

                displayAchievements(dayWithAchievements);
            }
        });
    }


    private LiveData<List<DayWithAchievements>> getDayWithAchievements() {
        return repository.getDayWithAchievementsList();
    }

    private void displayAchievements(List<DayWithAchievements> dayWithAchievementsList) {

        List<String> headerList = new ArrayList<>();
        HashMap<String, List<Achievement>> listHashMap = new HashMap<>();

        for (int i = 0; i < dayWithAchievementsList.size(); i++) {

            DayWithAchievements currentDayWithAchievements = dayWithAchievementsList.get(i);
            List<Achievement> currentDayAchievements = currentDayWithAchievements.achievementList;
            String currentHeader = currentDayWithAchievements.day.getHeader();

            headerList.add(currentHeader);

            listHashMap.put(currentHeader, currentDayAchievements);
        }

        adapter.setHeaderList(headerList);
        adapter.setListHashMap(listHashMap);

        adapter.notifyDataSetChanged();
    }
}

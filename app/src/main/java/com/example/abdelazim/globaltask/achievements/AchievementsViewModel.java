package com.example.abdelazim.globaltask.achievements;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.example.abdelazim.globaltask.repository.AppRepository;
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

        getAchievementsList().observe(owner, new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable List<Task> achievements) {

                displayAchievements(achievements);
            }
        });
    }


    private LiveData<List<Task>> getAchievementsList() {
        return repository.getAchievements();
    }

    private void displayAchievements(List<Task> achievements) {

        List<String> headerList = new ArrayList<>();
        headerList.add("23 October");
        adapter.setHeaderList(headerList);

        HashMap<String, List<Task>> hashMap = new HashMap<>();
        hashMap.put(headerList.get(0), achievements);
        adapter.setListHashMap(hashMap);

        adapter.notifyDataSetChanged();
    }
}

package com.example.abdelazim.globaltask.achievements;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.example.abdelazim.globaltask.repository.local.AppDatabase;
import com.example.abdelazim.globaltask.repository.model.Task;
import com.example.abdelazim.globaltask.tasks.TaskAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AchievementsViewModel extends ViewModel {


    private LiveData<List<Task>> achievementsList;
    private AchievementAdapter adapter;

    public void start(Context context, AchievementAdapter adapter) {

        this.adapter = adapter;
        achievementsList = AppDatabase.getInstance(context).taskDao().getAchievements();
    }

    public LiveData<List<Task>> getAchievementsList() {
        return achievementsList;
    }

    public void displayAchievements(List<Task> achievements) {

        List<String> headerList = new ArrayList<>();
        headerList.add("21 October");
        headerList.add("22 October");
        headerList.add("23 October");
        adapter.setHeaderList(headerList);
        HashMap<String, List<Task>> hashMap = new HashMap<>();
        hashMap.put(headerList.get(0), achievements);
        hashMap.put(headerList.get(1), achievements);
        hashMap.put(headerList.get(2), achievements);
        adapter.setListHashMap(hashMap);
        adapter.notifyDataSetChanged();
    }
}

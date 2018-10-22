package com.example.abdelazim.globaltask.tasks;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.example.abdelazim.globaltask.repository.local.AppDatabase;
import com.example.abdelazim.globaltask.repository.model.Task;

import java.util.List;

public class TasksViewModel extends ViewModel {

    private LiveData<List<Task>> taskList;
    private TaskAdapter adapter;

    public void start(Context context, TaskAdapter adapter) {

        this.adapter = adapter;
        taskList = AppDatabase.getInstance(context).taskDao().getTasks();
    }

    public LiveData<List<Task>> getTaskList() {
        return taskList;
    }

    public void displayTasks(List<Task> tasks) {

        adapter.setTaskList(tasks);
        adapter.notifyDataSetChanged();
    }
}

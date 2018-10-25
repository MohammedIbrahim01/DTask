package com.example.abdelazim.globaltask.tasks;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.example.abdelazim.globaltask.repository.AppRepository;
import com.example.abdelazim.globaltask.repository.model.Task;

import java.util.List;

public class TasksViewModel extends ViewModel {

    private AppRepository repository;
    private TaskAdapter adapter;

    public void start(AppRepository repository, TaskAdapter adapter) {

        this.repository = repository;
        this.adapter = adapter;
    }

    public void observe(LifecycleOwner owner) {

        getTaskList().observe(owner, new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable List<Task> tasks) {

                displayTasks(tasks);
            }
        });
    }

    private LiveData<List<Task>> getTaskList() {
        return repository.getTaskList();
    }

    private void displayTasks(List<Task> tasks) {

        adapter.setTaskList(tasks);
        adapter.notifyDataSetChanged();
    }

    public void finishTask(Task task) {

        repository.finishTask(task);
    }
}

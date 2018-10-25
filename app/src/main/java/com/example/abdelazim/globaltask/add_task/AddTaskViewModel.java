package com.example.abdelazim.globaltask.add_task;

import android.arch.lifecycle.ViewModel;

import com.example.abdelazim.globaltask.repository.AppRepository;
import com.example.abdelazim.globaltask.repository.model.Task;

public class AddTaskViewModel extends ViewModel {

    private AppRepository repository;

    public void start(AppRepository repository) {

        this.repository = repository;
    }

    public void saveTask(Task task) {

        repository.saveNewTask(task);
    }
}

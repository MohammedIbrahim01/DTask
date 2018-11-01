package com.example.abdelazim.globaltask.add_task;

import android.arch.lifecycle.ViewModel;

import com.example.abdelazim.globaltask.repository.AppRepository;
import com.example.abdelazim.globaltask.repository.model.Task;
import com.example.abdelazim.globaltask.utils.AppNotifications;

public class AddTaskViewModel extends ViewModel {

    private AppRepository repository;


    /**
     * Set the AppRepository
     *
     * @param repository AppRepository
     */

    void start(AppRepository repository) {

        this.repository = repository;
    }


    /**
     * Create new Task from params
     *
     * Save the task in the repository
     *
     * @param title
     * @param description
     * @param timeInMillis
     */
    void saveNewTask(String title, String description, long timeInMillis) {

        Task task = new Task(title, description, timeInMillis);

        repository.saveNewTask(task);
    }
}

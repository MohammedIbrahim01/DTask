package com.abdelazim.globaltask.add_task;

import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.abdelazim.globaltask.repository.AppRepository;
import com.abdelazim.globaltask.repository.model.Task;

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

        Task task = new Task((int) (timeInMillis % 1000), title, description, timeInMillis);

        repository.saveNewTask(task);
    }

    public void saveEditedTask(int id, String title, String description, long timeInMillis) {

        Task task = new Task(id, title, description, timeInMillis);

        repository.saveEditedTask(task);
    }

    public void deleteTask(int id, String title, String description, long timeInMillis) {

        Task task = new Task(id, title, description, timeInMillis);

        repository.delete(task);
    }

    public void publishTask(String title, String description, long timeInMillis, Context context) {

        Task task = new Task(title, description, timeInMillis);
        repository = AppRepository.getInstance(context);
        repository.publishTask(task);
    }
}

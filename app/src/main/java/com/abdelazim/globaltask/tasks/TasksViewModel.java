package com.abdelazim.globaltask.tasks;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.abdelazim.globaltask.repository.AppRepository;
import com.abdelazim.globaltask.repository.model.Task;

import java.util.List;

public class TasksViewModel extends ViewModel {

    private AppRepository repository;
    private TaskAdapter adapter;
    private TasksView view;


    /**
     * Set the AppRepository and taskAdapter
     *
     * @param repository AppRepository
     * @param adapter    TaskAdapter
     */
    public void start(AppRepository repository, TaskAdapter adapter, TasksView view) {

        this.repository = repository;
        this.adapter = adapter;
        this.view = view;
    }


    /**
     * Observe TaskList changes
     *
     * @param owner LifecycleOwner
     */
    void observe(LifecycleOwner owner) {

        repository.getTaskList().observe(owner, new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable List<Task> tasks) {

                if (tasks == null || tasks.size() == 0) {
                    view.displayNoTasksView();
                } else {
                    view.displayTasksView();
                    displayTasks(tasks);
                }
            }
        });
    }


    /**
     * Pass taskList to the adapter
     *
     * Then notifyAdapter
     *
     * @param taskList
     */
    private void displayTasks(List<Task> taskList) {

        adapter.setTaskList(taskList);
        adapter.notifyDataSetChanged();
    }


    /**
     * Pass finished task to repository to remove it from task table
     *
     * And put it in achievement table
     *
     * @param task
     */
    void TaskFinished(Task task) {

        repository.finishTask(task);
    }

    public interface TasksView {
        void displayNoTasksView();

        void displayTasksView();
    }
}

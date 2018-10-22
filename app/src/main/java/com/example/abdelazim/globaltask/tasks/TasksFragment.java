package com.example.abdelazim.globaltask.tasks;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abdelazim.globaltask.R;
import com.example.abdelazim.globaltask.repository.model.Task;

import java.util.List;

public class TasksFragment extends Fragment {

    private TasksViewModel mViewModel;
    private RecyclerView tasksRecyclerView;
    private TaskAdapter adapter;


    public static TasksFragment newInstance() {
        return new TasksFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tasks_fragment, container, false);

        setupRecyclerViewWithAdapter(view);

        mViewModel = ViewModelProviders.of(this).get(TasksViewModel.class);

        mViewModel.start(getContext(), adapter);

        mViewModel.getTaskList().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable List<Task> tasks) {

                mViewModel.displayTasks(tasks);
            }
        });

        return view;
    }


    /**
     * Get reference to recyclerView
     *
     * Instantiate TaskAdapter
     *
     * set adapter and LayoutManager
     *
     * @param view
     */
    private void setupRecyclerViewWithAdapter(View view) {

        adapter = new TaskAdapter();
        tasksRecyclerView = view.findViewById(R.id.tasks_recyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tasksRecyclerView.setAdapter(adapter);
        tasksRecyclerView.setHasFixedSize(true);
    }
}

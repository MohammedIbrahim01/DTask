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
import com.example.abdelazim.globaltask.main.MainViewModel;
import com.example.abdelazim.globaltask.repository.model.Task;

import java.util.List;

public class TasksFragment extends Fragment {

    // MainViewModel
    private MainViewModel mainViewModel;
    // ViewModel of this fragment
    private TasksViewModel mViewModel;
    // Views
    private RecyclerView tasksRecyclerView;
    private TaskAdapter adapter;


    public static TasksFragment newInstance() {
        return new TasksFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate fragment layout
        View view = inflater.inflate(R.layout.tasks_fragment, container, false);

        // Initialize views
        initViews(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get ViewModels
        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        mViewModel = ViewModelProviders.of(this).get(TasksViewModel.class);

        mViewModel.start(mainViewModel.getRepository(), adapter);

        mViewModel.observe(this);
    }

    private void initViews(View view) {

        tasksRecyclerView = view.findViewById(R.id.tasks_recyclerView);
        setupRecyclerViewWithAdapter();
    }


    /**
     * Get reference to recyclerView
     *
     * Instantiate TaskAdapter
     *
     * set adapter and LayoutManager
     */
    private void setupRecyclerViewWithAdapter() {

        adapter = new TaskAdapter();
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tasksRecyclerView.setHasFixedSize(true);
        tasksRecyclerView.setAdapter(adapter);
    }
}

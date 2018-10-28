package com.example.abdelazim.globaltask.tasks;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.abdelazim.globaltask.R;
import com.example.abdelazim.globaltask.main.MainViewModel;
import com.example.abdelazim.globaltask.repository.model.Task;
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TasksFragment extends Fragment implements View.OnClickListener, TasksViewModel.TasksView {

    // MainViewModel
    private MainViewModel mainViewModel;
    // ViewModel of this fragment
    private TasksViewModel mViewModel;
    // Views
    @BindView(R.id.add_task_fab)
    FloatingActionLayout addTaskFab;
    @BindView(R.id.done_fab)
    FloatingActionLayout doneFab;
    @BindView(R.id.no_tasks_view)
    RelativeLayout noTasksView;
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

        // Start viewModel
        mViewModel.start(mainViewModel.getRepository(), adapter, this);

        // Observe
        mViewModel.observe(this);
    }


    /**
     * Get views references
     *
     * Setup views
     *
     * @param view fragment layout that was inflated
     */
    private void initViews(View view) {

        ButterKnife.bind(this, view);

        tasksRecyclerView = view.findViewById(R.id.tasks_recyclerView);
        setupRecyclerViewWithAdapter();

        addTaskFab.setOnClickListener(this);
        doneFab.setOnClickListener(this);
    }


    /**
     * Instantiate TaskAdapter
     *
     * Setup recyclerView
     */
    private void setupRecyclerViewWithAdapter() {

        adapter = new TaskAdapter();
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tasksRecyclerView.setHasFixedSize(true);
        tasksRecyclerView.setAdapter(adapter);

        // Swipe when finish task functionality
        getItemTouchHelper().attachToRecyclerView(tasksRecyclerView);
    }


    /**
     * Setup swipe when finish task functionality
     *
     * @return
     */
    @NonNull
    private ItemTouchHelper getItemTouchHelper() {

        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                // Get task that was swiped
                Task task = adapter.getTaskList().get(viewHolder.getAdapterPosition());
                // Task is finished
                mViewModel.TaskFinished(task);
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.add_task_fab:
                mainViewModel.setScreen(2);
                break;
            case R.id.done_fab:
                mainViewModel.setScreen(0);
                break;
        }
    }

    @Override
    public void displayNoTasksView() {

        tasksRecyclerView.setVisibility(View.GONE);
        noTasksView.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayTasksView() {

        tasksRecyclerView.setVisibility(View.VISIBLE);
        noTasksView.setVisibility(View.GONE);
    }
}

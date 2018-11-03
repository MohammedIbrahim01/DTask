package com.example.abdelazim.globaltask.tasks;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.abdelazim.globaltask.R;
import com.example.abdelazim.globaltask.main.MainViewModel;
import com.example.abdelazim.globaltask.repository.model.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TasksFragment extends Fragment implements View.OnClickListener, TasksViewModel.TasksView, TaskAdapter.OnTaskItemClickListener {

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
    @BindView(R.id.tasks_recyclerView)
    RecyclerView tasksRecyclerView;
    @BindView(R.id.get_global_task_fab)
    FloatingActionLayout getGlobalTaskFab;

    private TaskAdapter adapter;
    private DatabaseReference globalTasksNode;


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

        globalTasksNode = FirebaseDatabase.getInstance().getReference().child("tasks");
        globalTasksNode.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                mViewModel.newGlobalTask(dataSnapshot.getValue(Task.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

        adapter = new TaskAdapter(this);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tasksRecyclerView.setHasFixedSize(true);
        tasksRecyclerView.setAdapter(adapter);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        tasksRecyclerView.addItemDecoration(decoration);


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

    @Override
    public void thereIsGlobalTask(final Task task) {

        getGlobalTaskFab.setVisibility(View.VISIBLE);
        getGlobalTaskFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mViewModel.addTask(task);
                Log.i("WWW", "task: " + task.getTitle());
                getGlobalTaskFab.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onTaskItemClick(int index) {

        Task task = adapter.getTaskList().get(index);
        mainViewModel.setScreen(4, task);
    }
}

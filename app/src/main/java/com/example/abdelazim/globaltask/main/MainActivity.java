package com.example.abdelazim.globaltask.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.abdelazim.globaltask.R;
import com.example.abdelazim.globaltask.achievements.AchievementsFragment;
import com.example.abdelazim.globaltask.add_task.AddTaskFragment;
import com.example.abdelazim.globaltask.tasks.TasksFragment;
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // ViewModel
    MainViewModel mainViewModel;
    // FragmentManager instance variable
    private FragmentManager fragmentManager;

    // Views
    @BindView(R.id.add_task_fab)
    FloatingActionLayout addTaskFab;
    @BindView(R.id.done_fab)
    FloatingActionLayout doneFab;
    @BindView(R.id.tasks_fab)
    FloatingActionLayout tasksFab;
    @BindView(R.id.back_fab)
    FloatingActionLayout backFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Get ViewModel
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // Get fragmentManager
        fragmentManager = getSupportFragmentManager();

        // Display TasksFragment as an start screen
        fragmentManager.beginTransaction()
                .add(R.id.main_frag_container, new TasksFragment())
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                .commit();
        setFabsVisibility(1, 1, 0, 0);

        // Handling clicks
        addTaskFab.setOnClickListener(this);
        doneFab.setOnClickListener(this);
        tasksFab.setOnClickListener(this);
        backFab.setOnClickListener(this);
    }


    private void setFabsVisibility(int done, int addTask, int back, int tasks) {

        doneFab.setVisibility(done == 1 ? View.VISIBLE : View.INVISIBLE);
        addTaskFab.setVisibility(addTask == 1 ? View.VISIBLE : View.INVISIBLE);
        backFab.setVisibility(back == 1 ? View.VISIBLE : View.INVISIBLE);
        tasksFab.setVisibility(tasks == 1 ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_task_fab:
                // Display AddTaskFragment
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.main_frag_container, new AddTaskFragment())
                        .commit();
                setFabsVisibility(0, 0, 1, 0);
                break;
            case R.id.done_fab:
                // Display AchievementsFragment
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.main_frag_container, new AchievementsFragment())
                        .commit();
                setFabsVisibility(0, 0, 0, 1);
                break;
            case R.id.tasks_fab:
                // Display TasksFragment
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.main_frag_container, new TasksFragment())
                        .commit();
                setFabsVisibility(1, 1, 0, 0);
                break;
            case R.id.back_fab:
                // Display TasksFragment
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.main_frag_container, new TasksFragment())
                        .commit();
                setFabsVisibility(1, 1, 0, 0);
                break;
        }
    }
}

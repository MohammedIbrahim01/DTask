package com.example.abdelazim.globaltask.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.abdelazim.globaltask.R;
import com.example.abdelazim.globaltask.achievements.AchievementsFragment;
import com.example.abdelazim.globaltask.add_task.AddTaskFragment;
import com.example.abdelazim.globaltask.repository.local.AppDatabase;
import com.example.abdelazim.globaltask.repository.model.Task;
import com.example.abdelazim.globaltask.settings.SettingsActivity;
import com.example.abdelazim.globaltask.tasks.TasksFragment;
import com.example.abdelazim.globaltask.utils.AppExecutors;
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionLayout;

import java.util.List;

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

        // Start ViewModel
        mainViewModel.start(getApplicationContext());

        // Get fragmentManager
        fragmentManager = getSupportFragmentManager();

        // Display TasksFragment as an start screen
        fragmentManager.beginTransaction()
                .replace(R.id.main_frag_container, new TasksFragment(), "tasks_frag")
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

        doneFab.setVisibility(done == 1 ? View.VISIBLE : View.GONE);
        addTaskFab.setVisibility(addTask == 1 ? View.VISIBLE : View.GONE);
        backFab.setVisibility(back == 1 ? View.VISIBLE : View.GONE);
        tasksFab.setVisibility(tasks == 1 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_task_fab:
                // Display AddTaskFragment
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                        .addToBackStack(null)
                        .replace(R.id.main_frag_container, new AddTaskFragment(), "add_task_frag")
                        .commit();
                setFabsVisibility(0, 0, 1, 0);
                break;
            case R.id.done_fab:
                // Display AchievementsFragment
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.main_frag_container, new AchievementsFragment(), "achievements_frag")
                        .commit();
                setFabsVisibility(0, 0, 0, 1);
                break;
            case R.id.tasks_fab:
                // Display TasksFragment
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.main_frag_container, new TasksFragment(), "tasks_frag")
                        .commit();
                setFabsVisibility(1, 1, 0, 0);
                break;
            case R.id.back_fab:
                // Display TasksFragment
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.main_frag_container, new TasksFragment(), "tasks_frag")
                        .commit();
                setFabsVisibility(1, 1, 0, 0);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // Display SettingsFragment
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

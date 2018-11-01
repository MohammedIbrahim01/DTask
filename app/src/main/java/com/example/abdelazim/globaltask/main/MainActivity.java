package com.example.abdelazim.globaltask.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.abdelazim.globaltask.R;
import com.example.abdelazim.globaltask.achievements.AchievementsFragment;
import com.example.abdelazim.globaltask.add_task.AddTaskFragment;
import com.example.abdelazim.globaltask.settings.SettingsActivity;
import com.example.abdelazim.globaltask.tasks.TasksFragment;

public class MainActivity extends AppCompatActivity implements MainViewModel.MainActivityView {

    // ViewModel
    MainViewModel mainViewModel;
    // FragmentManager instance variable
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get fragmentManager
        fragmentManager = getSupportFragmentManager();
        // Get ViewModel
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        // Start ViewModel
        mainViewModel.start(getApplicationContext(), this);
        // Display TasksFragment
        mainViewModel.setScreen(1);
        // Observe
        mainViewModel.observe(this);
    }


    /**
     * Create menu to display settings option
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    /**
     * Handle menu items selection
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // Display SettingsActivity
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Display the appropriate fragment
     *
     * @param screen
     */
    @Override
    public void display(int screen) {
        switch (screen) {
            case 0:
                // Display AchievementsFragment
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.main_frag_container, AchievementsFragment.newInstance())
                        .commit();
                break;
            case 1:
                // Display TasksFragment
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.main_frag_container, TasksFragment.newInstance())
                        .commit();
                break;
            case 2:
                // Display AddTaskFragment
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                        .addToBackStack(null)
                        .replace(R.id.main_frag_container, AddTaskFragment.newInstance())
                        .commit();
                break;

            case 3:
                fragmentManager.popBackStack();
                break;

        }
    }
}

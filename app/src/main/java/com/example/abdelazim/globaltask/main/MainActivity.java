package com.example.abdelazim.globaltask.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.abdelazim.globaltask.R;
import com.example.abdelazim.globaltask.achievements.AchievementsFragment;
import com.example.abdelazim.globaltask.add_task.AddTaskFragment;
import com.example.abdelazim.globaltask.repository.model.Task;
import com.example.abdelazim.globaltask.settings.SettingsActivity;
import com.example.abdelazim.globaltask.tasks.TasksFragment;
import com.example.abdelazim.globaltask.utils.AppConstants;
import com.example.abdelazim.globaltask.utils.AppExecutors;
import com.example.abdelazim.globaltask.utils.AppScheduler;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;

public class MainActivity extends AppCompatActivity implements MainViewModel.MainActivityView, SharedPreferences.OnSharedPreferenceChangeListener {

    // ViewModel
    MainViewModel mainViewModel;
    // FragmentManager instance variable
    private FragmentManager fragmentManager;
    // SharedPreferences
    private SharedPreferences sharedPreferences;
    // Ads
    AdView adView;
    private RewardedVideoAd mRewardedVideoAd;
    private int rewardedCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(AppConstants.DTASK_SHARED, MODE_PRIVATE);
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

        // Ads
        adView = findViewById(R.id.banner_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        loadRewardedVideoAd();
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-6179224755708033/5026782666",
                new AdRequest.Builder().build());
    }

    private void showRewardedVideoAd() {
        Log.i("WWW", "displayRewardedAd: " + rewardedCount);
        if (rewardedCount / 4 == 0) {
            rewardedCount++;
            return;
        }
        if (mRewardedVideoAd.isLoaded())
            mRewardedVideoAd.show();
        loadRewardedVideoAd();
        rewardedCount = 1;
    }


    @Override
    protected void onPause() {
        if (adView != null)
            adView.pause();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adView != null)
            adView.resume();

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    protected void onDestroy() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        if (adView != null)
            adView.destroy();
        super.onDestroy();
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
                showRewardedVideoAd();
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
                showRewardedVideoAd();
                fragmentManager.popBackStack();
                break;

        }
    }


    @Override
    public void display(int screen, Task task) {
        if (screen == 4) {
            AddTaskFragment editTaskFragment = AddTaskFragment.newInstance();
            Bundle args = new Bundle();
            args.putString("title", task.getTitle());
            args.putString("description", task.getDescription());
            args.putInt("taskId", task.getId());
            args.putLong("time", task.getTime());
            editTaskFragment.setArguments(args);
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                    .addToBackStack(null)
                    .replace(R.id.main_frag_container, editTaskFragment)
                    .commit();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Log.i("WWW", "reschedule wakeup notification");

        long wakeupTime = sharedPreferences.getLong(AppConstants.KEY_WAKEUP_TIME, 0);
        mainViewModel.rescheduleWakeupNotification(wakeupTime);
    }
}

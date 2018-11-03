package com.example.abdelazim.globaltask.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends AppCompatActivity implements MainViewModel.MainActivityView, SharedPreferences.OnSharedPreferenceChangeListener {

    // ViewModel
    MainViewModel mainViewModel;
    // FragmentManager instance variable
    private FragmentManager fragmentManager;
    // SharedPreferences
    private SharedPreferences sharedPreferences;
    // Ads
    AdView adView;
    InterstitialAd interstitialAd;
    private int interstitialCount = 0;


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

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        loadInterstitialAd();
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("WWW", "onAdLoaded: loaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                loadInterstitialAd();
            }
        });
    }

    private void loadInterstitialAd() {
        Log.i("WWW", "loadInterstitialAd");
        interstitialAd.loadAd(new AdRequest.Builder().build());
    }


    private void showInterstitialAd() {
        Log.i("WWW", "InterstitialAd: " + interstitialCount);
        if (interstitialCount / 2 == 0) {
            interstitialCount++;
            return;
        }
        if (interstitialAd.isLoaded())
            interstitialAd.show();

        interstitialCount = 0;
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
                showInterstitialAd();
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

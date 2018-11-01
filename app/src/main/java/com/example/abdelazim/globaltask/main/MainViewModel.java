package com.example.abdelazim.globaltask.main;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.abdelazim.globaltask.repository.AppRepository;
import com.example.abdelazim.globaltask.utils.AppScheduler;

import java.util.Calendar;

public class MainViewModel extends ViewModel {

    public static final String KEY_WAKEUP_TIME = "wakeup-time";
    public static final String DTASK_SHARED = "DTask-shared";
    public static final String NEED_FETCH = "need-fetch";

    private AppRepository repository;
    // Navigation
    private MutableLiveData<Integer> currentScreen = new MutableLiveData<>();
    // MainActivity View
    private MainActivityView mainActivityView;

    public void start(Context applicationContext, MainActivityView mainActivityView) {

        repository = AppRepository.getInstance(applicationContext);
        this.mainActivityView = mainActivityView;
        AppScheduler appScheduler = new AppScheduler(applicationContext);
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(DTASK_SHARED, Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("first-launch", true)) {

            appScheduler.scheduleTasksCleaner();
            appScheduler.scheduleFillTasksNotification(sharedPreferences.getLong(KEY_WAKEUP_TIME, Calendar.getInstance().getTimeInMillis()));
            sharedPreferences.edit().putBoolean("first-launch", false).apply();
        }
        if (sharedPreferences.getBoolean(NEED_FETCH, false)) {
            appScheduler.scheduleFillTasksNotification(sharedPreferences.getLong(KEY_WAKEUP_TIME, Calendar.getInstance().getTimeInMillis()));
            sharedPreferences.edit().putBoolean(NEED_FETCH, false).apply();
        }
    }

    public AppRepository getRepository() {
        return repository;
    }

    private LiveData<Integer> getCurrentScreen() {
        return currentScreen;
    }

    public void setScreen(Integer currentScreen) {
        this.currentScreen.postValue(currentScreen);
    }

    void observe(LifecycleOwner owner) {

        getCurrentScreen().observe(owner, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer screen) {

                mainActivityView.display(screen);
                Log.i("WWW", "onChanged: " + screen);
            }
        });
    }


    public interface MainActivityView {

        void display(int screen);
    }
}

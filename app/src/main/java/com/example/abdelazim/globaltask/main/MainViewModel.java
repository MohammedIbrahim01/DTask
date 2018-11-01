package com.example.abdelazim.globaltask.main;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.abdelazim.globaltask.repository.AppRepository;
import com.example.abdelazim.globaltask.utils.AppScheduler;

public class MainViewModel extends ViewModel {

    private AppRepository repository;
    // Navigation
    private MutableLiveData<Integer> currentScreen = new MutableLiveData<>();
    // MainActivity View
    private MainActivityView mainActivityView;

    public void start(Context applicationContext, MainActivityView mainActivityView) {

        repository = AppRepository.getInstance(applicationContext);
        this.mainActivityView = mainActivityView;
        AppScheduler appScheduler = new AppScheduler(applicationContext);
        appScheduler.scheduleTasksCleaner();
        appScheduler.scheduleFillTasksNotification();
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

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
import com.example.abdelazim.globaltask.repository.model.Task;
import com.example.abdelazim.globaltask.utils.AppConstants;
import com.example.abdelazim.globaltask.utils.AppNotifications;
import com.example.abdelazim.globaltask.utils.AppScheduler;

import java.util.Calendar;

import static com.example.abdelazim.globaltask.utils.AppTime.getExactCalendar;

public class MainViewModel extends ViewModel {


    private AppRepository repository;
    private AppNotifications appNotifications;
    // Navigation
    private MutableLiveData<Integer> screenNum = new MutableLiveData<>();
    // MainActivity View
    private MainActivityView view;
    private AppScheduler appScheduler;
    private SharedPreferences sharedPreferences;

    public void start(Context applicationContext, MainActivityView mainActivityView) {

        repository = AppRepository.getInstance(applicationContext);
        appNotifications = new AppNotifications(applicationContext);
        this.view = mainActivityView;

        // Get scheduler and SharedPreferences
        appScheduler = new AppScheduler(applicationContext);
        sharedPreferences = applicationContext.getSharedPreferences(AppConstants.DTASK_SHARED, Context.MODE_PRIVATE);


        if (sharedPreferences.getBoolean(AppConstants.KEY_FIRST_LAUNCH, true))
            firstLaunchSetup();
    }


    /**
     * Observe the screenNum
     *
     * @param owner
     */
    void observe(LifecycleOwner owner) {

        getScreenNum().observe(owner, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer screen) {

                view.display(screen);
            }
        });
    }


    /**
     * @return screenNum as LiveData Object to observe changes
     */
    private LiveData<Integer> getScreenNum() {
        return screenNum;
    }


    /**
     * To use in any fragments to display specific screen
     *
     * @param screenNum
     */
    public void setScreen(Integer screenNum) {
        this.screenNum.postValue(screenNum);
    }


    public void setScreen(int screen, Task task) {
        view.display(screen, task);
    }


    /**
     * If wakeup time has changed, re-schedule wakeup notification
     */
    public void rescheduleWakeupNotification(long wakeupTime) {
        appScheduler.scheduleWakeupNotification(wakeupTime);
    }


    /**
     * For first launch put initial values in SharedPreferences
     *
     * And Schedule wakeup notification and tasks cleaner
     */
    private void firstLaunchSetup() {
        Log.i("WWW", "first launch setup");
        // SharedPreferences: Set wake up time to 8:00 am
        Calendar calendar = getExactCalendar(8);
        sharedPreferences.edit().putLong(AppConstants.KEY_WAKEUP_TIME, calendar.getTimeInMillis()).apply();
        // Remove late Tasks from tasks list at end of the day
        appScheduler.scheduleTasksCleaner();
        // Remind the user at wake up time to set day tasks
        long wakeupTime = sharedPreferences.getLong(AppConstants.KEY_WAKEUP_TIME, 0);
        appScheduler.scheduleWakeupNotification(wakeupTime);
        // SharedPreferences: Set first launch to false
        sharedPreferences.edit().putBoolean(AppConstants.KEY_FIRST_LAUNCH, false).apply();
    }


    /**
     * To make appRepository reachable from the three fragment
     *
     * @return AppRepository
     */
    public AppRepository getRepository() {
        return repository;
    }


    /**
     * To make appNotifications reachable from the three fragment
     *
     * @return AppNotification
     */
    public AppNotifications getAppNotifications() {
        return appNotifications;
    }


    /**
     * View interface to pass navigation actions to the MainActivity
     */
    public interface MainActivityView {

        void display(int screen);

        void display(int screen, Task task);
    }
}

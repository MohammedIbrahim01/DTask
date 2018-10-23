package com.example.abdelazim.globaltask.main;

import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.example.abdelazim.globaltask.repository.AppRepository;

public class MainViewModel extends ViewModel {

    private AppRepository repository;

    public void start(Context applicationContext) {

        repository = AppRepository.getInstance(applicationContext);
    }

    public AppRepository getRepository() {
        return repository;
    }
}

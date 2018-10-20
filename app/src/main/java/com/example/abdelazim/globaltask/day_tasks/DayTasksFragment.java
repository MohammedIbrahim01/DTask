package com.example.abdelazim.globaltask.day_tasks;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abdelazim.globaltask.R;

public class DayTasksFragment extends Fragment {

    private DayTasksViewModel mViewModel;

    public static DayTasksFragment newInstance() {
        return new DayTasksFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.day_tasks_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(DayTasksViewModel.class);
        // TODO: Use the ViewModel
    }

}

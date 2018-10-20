package com.example.abdelazim.globaltask.all_achievements;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abdelazim.globaltask.R;

public class AllAchievementsFragment extends Fragment {

    private AllAchievementsViewModel mViewModel;

    public static AllAchievementsFragment newInstance() {
        return new AllAchievementsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.all_achievements_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AllAchievementsViewModel.class);
        // TODO: Use the ViewModel
    }

}

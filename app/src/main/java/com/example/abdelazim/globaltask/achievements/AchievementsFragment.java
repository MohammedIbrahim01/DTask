package com.example.abdelazim.globaltask.achievements;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.abdelazim.globaltask.R;
import com.example.abdelazim.globaltask.repository.model.Task;

import java.util.List;

public class AchievementsFragment extends Fragment {

    private AchievementsViewModel mViewModel;
    private ExpandableListView achievementsExpandableListView;
    private AchievementAdapter adapter;

    public static AchievementsFragment newInstance() {
        return new AchievementsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.achievements_fragment, container, false);

        setupExpandableListViewWithAdapter(view);

        mViewModel = ViewModelProviders.of(this).get(AchievementsViewModel.class);

        mViewModel.start(getContext(), adapter);

        mViewModel.getAchievementsList().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable List<Task> achievements) {

                mViewModel.displayAchievements(achievements);
            }
        });

        achievementsExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
                parent.setItemChecked(groupPosition, true);
                return true;
            }
        });

        return view;
    }

    private void setupExpandableListViewWithAdapter(View view) {

        adapter = new AchievementAdapter();
        achievementsExpandableListView = view.findViewById(R.id.achievements_expandableListView);
        achievementsExpandableListView.setAdapter(adapter);
    }
}

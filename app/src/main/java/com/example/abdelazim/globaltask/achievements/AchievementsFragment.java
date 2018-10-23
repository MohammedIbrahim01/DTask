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
import com.example.abdelazim.globaltask.main.MainViewModel;
import com.example.abdelazim.globaltask.repository.model.Task;

import java.util.List;

public class AchievementsFragment extends Fragment {

    // MainViewModel
    private MainViewModel mainViewModel;
    // ViewModel of this fragment
    private AchievementsViewModel mViewModel;
    // Views
    private ExpandableListView achievementsExpandableListView;
    private AchievementAdapter adapter;

    public static AchievementsFragment newInstance() {
        return new AchievementsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate fragment layout
        View view = inflater.inflate(R.layout.achievements_fragment, container, false);

        // Initialize Views
        initViews(view);

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get ViewModels
        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        mViewModel = ViewModelProviders.of(this).get(AchievementsViewModel.class);

        mViewModel.start(mainViewModel.getRepository(), adapter);

        mViewModel.observe(this);
    }

    private void initViews(View view) {

        achievementsExpandableListView = view.findViewById(R.id.achievements_expandableListView);
        setupExpandableListViewWithAdapter();
    }


    private void setupExpandableListViewWithAdapter() {

        adapter = new AchievementAdapter();
        achievementsExpandableListView.setAdapter(adapter);
    }
}

package com.example.abdelazim.globaltask.achievements;

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
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AchievementsFragment extends Fragment implements View.OnClickListener {

    // MainViewModel
    private MainViewModel mainViewModel;
    // ViewModel of this fragment
    private AchievementsViewModel mViewModel;
    // Views
    @BindView(R.id.tasks_fab)
    FloatingActionLayout tasksFab;
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

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get ViewModels
        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        mViewModel = ViewModelProviders.of(this).get(AchievementsViewModel.class);

        // Start viewModel
        mViewModel.start(mainViewModel.getRepository(), adapter);

        // Observe
        mViewModel.observe(this);
    }


    /**
     * Get views references
     *
     * Setup views
     *
     * @param view fragment layout that was inflated
     */
    private void initViews(View view) {

        ButterKnife.bind(this, view);

        achievementsExpandableListView = view.findViewById(R.id.achievements_expandableListView);
        setupExpandableListViewWithAdapter();

        tasksFab.setOnClickListener(this);
    }


    /**
     * Instantiate TaskAdapter
     *
     * Setup recyclerView
     */
    private void setupExpandableListViewWithAdapter() {

        adapter = new AchievementAdapter();
        achievementsExpandableListView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tasks_fab:
                mainViewModel.setScreen(1);
                break;
        }
    }
}

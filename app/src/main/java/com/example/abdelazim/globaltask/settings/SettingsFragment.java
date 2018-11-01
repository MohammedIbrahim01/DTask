package com.example.abdelazim.globaltask.settings;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.abdelazim.globaltask.R;
import com.example.abdelazim.globaltask.utils.AppConstants;
import com.example.abdelazim.globaltask.utils.AppFormatter;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsFragment extends Fragment implements TimePickerDialog.OnTimeSetListener {


    private SharedPreferences sharedPreferences;
    // Views
    @BindView(R.id.wakeup_layout)
    LinearLayout wakeupLayout;
    @BindView(R.id.wakeup_summary)
    TextView wakeupSummaryTextView;

    private Calendar calendar;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.settings_fragment, container, false);

        ButterKnife.bind(this, view);


        calendar = Calendar.getInstance();
        long time = sharedPreferences.getLong(AppConstants.KEY_WAKEUP_TIME, 0);
        calendar.setTimeInMillis(time);

        wakeupSummaryTextView.setText(AppFormatter.formatTime(calendar.getTimeInMillis()));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sharedPreferences = context.getSharedPreferences(AppConstants.DTASK_SHARED, Context.MODE_PRIVATE);
    }

    @Override
    public void onStart() {
        super.onStart();

        wakeupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getContext(), SettingsFragment.this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        wakeupSummaryTextView.setText(AppFormatter.formatTime(hourOfDay, minute));
        sharedPreferences.edit().putLong(AppConstants.KEY_WAKEUP_TIME, calendar.getTimeInMillis()).apply();
        // Mark wakeup time as changed
        sharedPreferences.edit().putBoolean(AppConstants.WAKEUP_TIME_CHANGED, true).apply();
    }
}

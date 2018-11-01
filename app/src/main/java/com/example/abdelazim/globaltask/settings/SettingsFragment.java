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
import com.example.abdelazim.globaltask.utils.AppFormatter;

import java.util.Calendar;

public class SettingsFragment extends Fragment implements TimePickerDialog.OnTimeSetListener {

    public static final String KEY_WAKEUP_TIME = "wakeup-time";
    public static final String DTASK_SHARED = "DTask-shared";
    public static final String NEED_FETCH = "need-fetch";
    private SharedPreferences sharedPreferences;
    private LinearLayout wakeupLayout;
    private TextView wakeupSummaryTextView;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        wakeupLayout = view.findViewById(R.id.wakeup_layout);
        wakeupSummaryTextView = view.findViewById(R.id.wakeup_summary);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sharedPreferences = context.getSharedPreferences(DTASK_SHARED, Context.MODE_PRIVATE);
    }

    @Override
    public void onStart() {
        super.onStart();

        wakeupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new TimePickerDialog(getContext(), SettingsFragment.this, 8, 0, false).show();
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        wakeupSummaryTextView.setText(AppFormatter.formatTime(hourOfDay, minute));
        sharedPreferences.edit().putLong(KEY_WAKEUP_TIME, calendar.getTimeInMillis()).apply();
        sharedPreferences.edit().putBoolean(NEED_FETCH, true).apply();
    }
}

package com.example.abdelazim.globaltask.add_task;

import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.abdelazim.globaltask.R;
import com.example.abdelazim.globaltask.main.MainViewModel;
import com.example.abdelazim.globaltask.repository.model.Task;
import com.example.abdelazim.globaltask.utils.AppFormatter;

import java.util.Calendar;

public class AddTaskFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    // MainViewModel
    private MainViewModel mainViewModel;
    // ViewModel of this fragment
    private AddTaskViewModel mViewModel;

    private Calendar calendar;

    // Views
    private EditText titleEditText, descriptionEditText;
    private Button setTimeButton, saveButton;
    private TextView timeTextView;
    private RelativeLayout timeLayout;
    private TimePickerDialog timePickerDialog;

    public static AddTaskFragment newInstance() {
        return new AddTaskFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate fragment layout
        View view = inflater.inflate(R.layout.add_task_fragment, container, false);

        // Initialize views
        initViews(view);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get ViewModels
        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        mViewModel = ViewModelProviders.of(this).get(AddTaskViewModel.class);

        // Start ViewModel
        mViewModel.start(mainViewModel.getRepository());
    }

    /**
     * Get views references
     *
     * Set onclick listeners
     *
     * Setup initial visibility
     */
    private void initViews(View view) {

        titleEditText = view.findViewById(R.id.title_editText);
        descriptionEditText = view.findViewById(R.id.description_editText);
        setTimeButton = view.findViewById(R.id.set_time_button);
        saveButton = view.findViewById(R.id.save_button);
        timeTextView = view.findViewById(R.id.time_textView);
        timeLayout = view.findViewById(R.id.time_layout);

        setTimeButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        setupTimeLayout(0, 0, false);
    }


    /**
     * If time was set hide setTime button
     *
     * Set text of timeTextView to the appropriate formatted time
     *
     * Display time layout
     *
     * @param hourOfDay
     * @param minute
     * @param visible
     */
    private void setupTimeLayout(int hourOfDay, int minute, boolean visible) {

        timeTextView.setText(AppFormatter.formatTime(hourOfDay, minute));

        setTimeButton.setVisibility(visible ? View.GONE : View.VISIBLE);
        timeLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
    }


    /**
     * Display PickTimeDialog
     */
    private void startPickTime() {
        calendar = Calendar.getInstance();
        timePickerDialog = new TimePickerDialog(getContext(), this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }


    /**
     * Get the title and description and the time
     *
     * Save new Task
     *
     * Go back to tasks fragment
     */
    private void saveNewTask() {

        String title = titleEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        mViewModel.saveNewTask(title, description, calendar.getTimeInMillis());

        getActivity().getSupportFragmentManager().popBackStack();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.set_time_button:
                startPickTime();
                break;

            case R.id.save_button:
                // Check if calendar is null meaning that the user didn't set time yet
                if (calendar == null) {
                    Toast.makeText(getContext(), "don't forget to set time", Toast.LENGTH_SHORT).show();
                    break;
                }
                saveNewTask();
                break;
        }
    }


    /**
     * Set the calendar fields to the hour and minute that user chose
     *
     * Display the formatted time
     *
     * Set views visibilities as timeSet = true
     *
     * @param view
     * @param hourOfDay
     * @param minute
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        // Set calendar fields
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        setupTimeLayout(hourOfDay, minute, true);
    }
}

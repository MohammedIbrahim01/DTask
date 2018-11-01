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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.abdelazim.globaltask.R;
import com.example.abdelazim.globaltask.main.MainViewModel;
import com.example.abdelazim.globaltask.utils.AppFormatter;
import com.example.abdelazim.globaltask.utils.AppNotifications;
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionLayout;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddTaskFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    // MainViewModel
    private MainViewModel mainViewModel;
    // ViewModel of this fragment
    private AddTaskViewModel mViewModel;

    private Calendar calendar;

    // Views
    private EditText titleEditText, descriptionEditText;
    private Button saveButton;
    private TextView timeTextView;
    private ImageView editImageView;
    private TimePickerDialog timePickerDialog;
    @BindView(R.id.back_fab)
    FloatingActionLayout backFab;

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
        mViewModel.start(mainViewModel.getRepository(), new AppNotifications(getContext()));
    }

    /**
     * Get views references
     *
     * Set onclick listeners
     *
     * Setup initial visibility
     */
    private void initViews(View view) {

        ButterKnife.bind(this, view);

        // Set the initial time to display in the time textView
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 1);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        titleEditText = view.findViewById(R.id.title_editText);
        descriptionEditText = view.findViewById(R.id.description_editText);
        saveButton = view.findViewById(R.id.save_button);
        timeTextView = view.findViewById(R.id.time_textView);
        editImageView = view.findViewById(R.id.edit_imageView);

        timeTextView.setText(AppFormatter.formatTime(calendar.getTimeInMillis()));

        timeTextView.setOnClickListener(this);
        editImageView.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        backFab.setOnClickListener(this);
    }


    /**
     * Display PickTimeDialog
     */
    private void startPickTime() {
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

        mainViewModel.setScreen(3);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.time_textView:
            case R.id.edit_imageView:
                startPickTime();
                break;

            case R.id.save_button:
                if (thereIsEmptyFields()) break;
                saveNewTask();
                break;

            case R.id.back_fab:
                mainViewModel.setScreen(3);
                break;
        }
    }

    private boolean thereIsEmptyFields() {
        if (titleEditText.getText().toString().isEmpty()) {
            titleEditText.setError("Task title is required");
            titleEditText.requestFocus();
            return true;
        }
        if (descriptionEditText.getText().toString().isEmpty()) {
            descriptionEditText.setError("Task description is required");
            descriptionEditText.requestFocus();
            return true;
        }
        if (descriptionEditText.getText().toString().length() < 10) {
            descriptionEditText.setError("Task description is too short");
            descriptionEditText.requestFocus();
            return true;
        }
        // Check if calendar is null meaning that the user didn't set time yet
        if (calendar == null) {
            Toast.makeText(getContext(), "don't forget to set time", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
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
        // Display formatted time
        timeTextView.setText(AppFormatter.formatTime(hourOfDay, minute));
    }
}

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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.abdelazim.globaltask.R;
import com.example.abdelazim.globaltask.main.MainViewModel;
import com.example.abdelazim.globaltask.utils.AppFormatter;
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionLayout;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddTaskFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    // MainViewModel
    private MainViewModel mainViewModel;
    // ViewModel of this fragment
    private AddTaskViewModel mViewModel;
    // ViewModel
    private Calendar calendar;

    // Views
    @BindView(R.id.text_new)
    TextView newTextView;
    @BindView(R.id.title_editText)
    EditText titleEditText;
    @BindView(R.id.description_editText)
    EditText descriptionEditText;
    @BindView(R.id.save_button)
    Button saveButton;
    @BindView(R.id.delete_button)
    Button deleteButton;
    @BindView(R.id.time_textView)
    TextView timeTextView;
    @BindView(R.id.edit_imageView)
    ImageView editImageView;
    @BindView(R.id.back_fab)
    FloatingActionLayout backFab;

    private TimePickerDialog timePickerDialog;
    private boolean editMode = false;
    private int taskId;
    private boolean publishMode = false;


    /**
     * @return new Instance of AddTaskFragment
     */
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

        ButterKnife.bind(this, view);

        // Set the initial time to display in the time textView
        Calendar calendar = Calendar.getInstance();
        timeTextView.setText(AppFormatter.formatTime(calendar.getTimeInMillis()));


        timeTextView.setOnClickListener(this);
        editImageView.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        backFab.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

        Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.containsKey("publish-mode")) {

                publishMode = true;
                saveButton.setText("publish");
                newTextView.setText("publish");
                return;
            }
            editMode = true;
            titleEditText.setText(arguments.getString("title"));
            descriptionEditText.setText(arguments.getString("description"));
            taskId = arguments.getInt("taskId");
            this.calendar = Calendar.getInstance();
            this.calendar.setTimeInMillis(arguments.getLong("time"));
            timeTextView.setText(AppFormatter.formatTime(this.calendar.getTimeInMillis()));
            saveButton.setText("edit");
            newTextView.setText("Edit");
            deleteButton.setVisibility(View.VISIBLE);
        }
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

        mainViewModel.setScreen(3);
    }

    /**
     * Get the title and description and the time
     *
     * Save new Task
     *
     * Go back to tasks fragment
     */
    private void saveEditedTask() {

        String title = titleEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        mViewModel.saveEditedTask(taskId, title, description, calendar.getTimeInMillis());

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
                if (editMode) {
                    saveEditedTask();
                    break;

                } else if (publishMode) {
                    publishTask();
                } else {
                    saveNewTask();
                }
                break;

            case R.id.back_fab:
                mainViewModel.setScreen(3);
                break;
            case R.id.delete_button:
                deleteTask();
        }
    }

    private void publishTask() {

        String title = titleEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        mViewModel.publishTask(title, description, calendar.getTimeInMillis(), getContext());

        getActivity().finish();
    }

    private void deleteTask() {

        String title = titleEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        mViewModel.deleteTask(taskId, title, description, calendar.getTimeInMillis());

        mainViewModel.setScreen(3);
    }

    private boolean thereIsEmptyFields() {
        if (titleEditText.getText().toString().isEmpty()) {
            titleEditText.setError("Task title is required");
            titleEditText.requestFocus();
            return true;
        } else if (descriptionEditText.getText().toString().isEmpty()) {
            descriptionEditText.setError("Task description is required");
            descriptionEditText.requestFocus();
            return true;
        } else if (descriptionEditText.getText().toString().length() < 10) {
            descriptionEditText.setError("Task description is too short");
            descriptionEditText.requestFocus();
            return true;
        }
        // Check if calendar is null meaning that the user didn't set time yet
        else if (calendar == null) {
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

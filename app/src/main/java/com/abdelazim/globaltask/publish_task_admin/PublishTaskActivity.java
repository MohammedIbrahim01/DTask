package com.abdelazim.globaltask.publish_task_admin;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.abdelazim.globaltask.R;
import com.abdelazim.globaltask.add_task.AddTaskFragment;

public class PublishTaskActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_task);

        fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {

            AddTaskFragment fragment = AddTaskFragment.newInstance();
            Bundle args = new Bundle();
            args.putBoolean("publish-mode", true);
            fragment.setArguments(args);
            fragmentManager.beginTransaction()
                    .replace(R.id.publish_task_frag_container, fragment)
                    .commit();
        }
    }
}

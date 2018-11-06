package com.abdelazim.globaltask.settings;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.abdelazim.globaltask.R;

public class SettingsActivity extends AppCompatActivity {

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        fragmentManager = getSupportFragmentManager();

        // Display SettingsFragment
        fragmentManager.beginTransaction()
                .replace(R.id.settings_frag_container, SettingsFragment.newInstance())
                .commit();

        // Setup actionbar
        setupActionbar();
    }

    private void setupActionbar() {

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

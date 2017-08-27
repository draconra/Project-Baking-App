package com.draconra.bakingapp.view;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.draconra.bakingapp.R;
import com.draconra.bakingapp.view.core.BaseActivity;

public class SettingsPrefActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_pref_layout_activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

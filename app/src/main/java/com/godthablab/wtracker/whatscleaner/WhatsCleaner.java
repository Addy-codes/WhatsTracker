package com.godthablab.wtracker.whatscleaner;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.godthablab.wtracker.R;

public class WhatsCleaner extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.whatscleaner);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_whatsclean);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }



    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}



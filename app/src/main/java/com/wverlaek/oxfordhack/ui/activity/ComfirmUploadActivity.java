package com.wverlaek.oxfordhack.ui.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wverlaek.oxfordhack.R;

public class ComfirmUploadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comfirm_upload);

        setTitle("Choose a challenge");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}


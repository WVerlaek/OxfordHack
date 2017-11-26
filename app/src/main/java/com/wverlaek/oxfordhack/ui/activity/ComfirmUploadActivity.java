package com.wverlaek.oxfordhack.ui.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.microsoft.projectoxford.vision.contract.Tag;
import com.wverlaek.oxfordhack.R;
import com.wverlaek.oxfordhack.vision.Picture;

public class ComfirmUploadActivity extends AppCompatActivity {

    private static Picture picture = null;
    private static Tag tag = null;

    public static void setPicture(Picture picture) {
        ComfirmUploadActivity.picture = picture;
    }

    public static void setTag(Tag tag) {
        ComfirmUploadActivity.tag = tag;
    }

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


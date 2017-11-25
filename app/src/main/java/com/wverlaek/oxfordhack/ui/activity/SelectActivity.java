package com.wverlaek.oxfordhack.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.microsoft.projectoxford.vision.contract.Tag;
import com.wverlaek.oxfordhack.R;
import com.wverlaek.oxfordhack.util.TextUtil;
import com.wverlaek.oxfordhack.vision.TagDetector;

import java.util.List;

public class SelectActivity extends AppCompatActivity {
    private static final String TAG = "SelectActivity";

    private TextView tagsTextView;
    private TagDetector tagDetector;
    private FrameLayout previewLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_select);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show());

        previewLayout = findViewById(R.id.camera);
        tagsTextView = findViewById(R.id.tags);

        tagDetector = new TagDetector(this, previewLayout);

        tagDetector.getDetectedTags().observe(this, this::handleResult);
        tagDetector.getError().observe(this, throwable -> {
            if (throwable != null) {
                Log.e(TAG, "Error in tag detector", throwable);
                tagsTextView.setBackgroundColor(Color.RED);
            } else {
                tagsTextView.setBackgroundColor(Color.WHITE);
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();

        tagDetector.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        tagDetector.resume(this);
    }



    private void handleResult(@Nullable List<Tag> tags) {
        String text = "";
        if (tags == null) {
            text = "Loading...";
        } else if (tags.isEmpty()) {
            text = "No tags found!";
        } else {
            StringBuilder sb = new StringBuilder();
            for (Tag tag : tags) {
                sb.append(tag.name).append(" ").append(TextUtil.formatDouble(tag.confidence)).append(", ");
            }
            text = sb.toString();
        }

        tagsTextView.setText(text);
    }

}

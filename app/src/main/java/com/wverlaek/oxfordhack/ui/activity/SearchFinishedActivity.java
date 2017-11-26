package com.wverlaek.oxfordhack.ui.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.projectoxford.vision.contract.Tag;
import com.wverlaek.oxfordhack.R;
import com.wverlaek.oxfordhack.util.TextUtil;
import com.wverlaek.oxfordhack.vision.Picture;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SearchFinishedActivity extends AppCompatActivity {
    private static Picture picture = null;
    private static String tag = null;

    public static final String KEY_TAG = "key_tag";
    public static final String KEY_PICTURE = "key_picture";

    private ImageView snapshot;
    private TextView tagCorrectView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_finished);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("");

//        Intent intent = getIntent();
//        Bundle extras = intent.getExtras();
//        if (extras != null) {
//            tag = extras.getString(KEY_TAG, null);
//            byte[] bytes = intent.hasExtra(KEY_PICTURE) ? extras.getByteArray(KEY_PICTURE) : null;
//            picture = bytes == null ? null : new Picture(bytes, false);
//        }

        if (picture == null || tag == null) {
            Toast.makeText(this, "Search activity needs a picture to start. Use setPicture(picture) first. tag=" + tag + " pic=" + picture, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        snapshot = findViewById(R.id.snapshot);
        snapshot.setImageBitmap(picture.getBitmap());

        tagCorrectView = findViewById(R.id.tag_correct);
        tagCorrectView.setText(TextUtil.capitalizeFirstLetter(tag));

        View mainMenuButton = findViewById(R.id.main_menu_button);
        mainMenuButton.setOnClickListener(view -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    public static void setPicture(Picture picture) {
        SearchFinishedActivity.picture = picture;
    }

    public static void setTag(Tag tag) {
        SearchFinishedActivity.tag = tag.name;
    }
}
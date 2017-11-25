package com.wverlaek.oxfordhack.ui.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
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
    private static Tag tag = null;

    private ImageView snapshot;
    private LinearLayout tagsLayout;
    private LinearLayout dismisserLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_finished);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Congratulations, you found the object!");

        if (picture == null || tag == null) {
            Toast.makeText(this, "Search activity needs a picture to start. Use setPicture(picture) first.", Toast.LENGTH_SHORT).show();
            finish();
        }

        snapshot = findViewById(R.id.snapshot);
        snapshot.setImageBitmap(picture.getBitmap());
        dismisserLayout = findViewById(R.id.dismisser);

        tagsLayout = findViewById(R.id.tags_layout);
        View tagView = LayoutInflater.from(this).inflate(R.layout.tag_view, tagsLayout, false);

        TextView tagName = tagView.findViewById(R.id.tag_name);
        tagName.setText(TextUtil.capitalizeFirstLetter(tag.name));
        tagsLayout.addView(tagView);

        FloatingActionButton doneFab = findViewById(R.id.done_fab);
        doneFab.setOnClickListener(view -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    public static void setPicture(Picture picture) {
        SearchFinishedActivity.picture = picture;
    }

    public static void setTag(Tag tag) {
        SearchFinishedActivity.tag = tag;
    }
}
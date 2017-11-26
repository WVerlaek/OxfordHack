package com.wverlaek.oxfordhack.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.projectoxford.vision.contract.Tag;
import com.wverlaek.oxfordhack.R;
import com.wverlaek.oxfordhack.util.Constants;
import com.wverlaek.oxfordhack.util.TagUtil;
import com.wverlaek.oxfordhack.util.TextUtil;
import com.wverlaek.oxfordhack.vision.Picture;
import com.wverlaek.oxfordhack.vision.TagDetector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SelectActivity extends AppCompatActivity {
    private static final String TAG = "SelectActivity";

    private TagDetector tagDetector;
    private FrameLayout previewLayout;
    private LinearLayout tagsLayout;
    private LinearLayout selectedTagLayout;
    private TextView loadingText;
    private TextView selectedTagNameTextView;
    private TextView errorMessage;

    private Tag selectedTag;
    private Picture selectedPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("I spy with my little eye ...");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        previewLayout = findViewById(R.id.camera);
        tagsLayout = findViewById(R.id.tags_layout);
        selectedTagLayout = findViewById(R.id.selected_tag);
        loadingText = findViewById(R.id.loading_text);
        selectedTagNameTextView = findViewById(R.id.selected_tag_name);
        errorMessage = findViewById(R.id.error_message);

        tagDetector = new TagDetector(this, previewLayout);

        tagDetector.getDetectedTags().observe(this, this::handleResult);
        tagDetector.getError().observe(this, throwable -> {
            errorMessage.setVisibility(throwable == null ? View.GONE : View.VISIBLE);

            if (throwable != null) {
                Log.e(TAG, "Error in tag detector", throwable);
            }
        });

        FloatingActionButton doneFab = findViewById(R.id.done_fab);
        doneFab.setOnClickListener(view -> {
            if (selectedPicture == null || selectedTag == null) {
                Toast.makeText(this, "No picture found. Tag=" + selectedTag + " pic=" + selectedPicture, Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, ConfirmUploadActivity.class);
//            intent.putExtra(SearchFinishedActivity.KEY_TAG, selectedTag.name);
//            intent.putExtra(SearchFinishedActivity.KEY_PICTURE, selectedPicture.getJpegData());
            ConfirmUploadActivity.setPicture(selectedPicture);
            ConfirmUploadActivity.setTag(selectedTag.name);
            startActivity(intent);
            finish();
        });

        setSelectedTag(null, null);
        setIsLoading(true);
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

    private void handleResult(@Nullable Pair<Picture, List<Tag>> pair) {
        List<Tag> tags = pair == null ? null : pair.second;
        Picture pic = pair == null ? null : pair.first;

        if (tags != null) {
            setIsLoading(false);
            tags = TagUtil.filter(tags, Constants.MIN_TAG_CONFIDENCE_SELECT);
            setTags(pic, tags);
        }
    }

    private void setTags(Picture picture, List<Tag> tags) {
        // sort on confidence decreasing order
        TagUtil.sort(tags);

        tagsLayout.removeAllViews();

        for (Tag tag : tags) {
            View tagView = LayoutInflater.from(this).inflate(R.layout.tag_view, tagsLayout, false);

            TextView tagName = tagView.findViewById(R.id.tag_name);
            tagName.setText(TextUtil.capitalizeFirstLetter(tag.name));

            tagName.setOnClickListener(view -> {
                setSelectedTag(tag, picture);
            });

            tagsLayout.addView(tagView);
        }
    }

    private void setSelectedTag(Tag tag, Picture picture) {
        if (tag != null) {
            selectedTagNameTextView.setText(TextUtil.capitalizeFirstLetter(tag.name));

            // only update when non null
            this.selectedTag = tag;
            this.selectedPicture = picture;
        }

        selectedTagLayout.setVisibility(tag == null ? View.GONE : View.VISIBLE);
    }

    private void setIsLoading(boolean loading) {
        loadingText.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

}

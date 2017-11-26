package com.wverlaek.oxfordhack.ui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.List;

public class SearchActivity extends AppCompatActivity {
    public static final String TARGET_TAG = "SEARCH_TARGET_TAG";
    private static final String TAG = "SearchActivity";
    private static final int COLD = 0;
    private static final int NEUTRAL = 1;
    private static final int WARM = 2;

    private TagDetector tagDetector;
    private FrameLayout previewLayout;
    private LinearLayout tagsLayout;
    private LinearLayout selectedTagLayout;
    private TextView loadingText;
    private TextView selectedTagNameTextView;
    private Toolbar toolbar;

    // The tag name that the user needs to find
    private String targetName;
    // The currently selected tag
    private Tag selectedTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(TARGET_TAG)) return;

        targetName = intent.getStringExtra(TARGET_TAG);

        setContentView(R.layout.activity_search);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Find the target!");

        previewLayout = findViewById(R.id.camera);
        tagsLayout = findViewById(R.id.tags_layout);
        selectedTagLayout = findViewById(R.id.selected_tag);
        selectedTagNameTextView = findViewById(R.id.selected_tag_name);
        loadingText = findViewById(R.id.loading_text);

        tagDetector = new TagDetector(this, previewLayout);

        tagDetector.getDetectedTags().observe(this, this::handleResult);
        tagDetector.getError().observe(this, throwable -> {
            if (throwable != null) {
                // Whoops! An error occurred while trying to perform magic on your image. Please check your internet connection and try again.
                // TODO: show an error message in UI
                Log.e(TAG, "Error in tag detector", throwable);
            } else {
                // TODO: hide error message
            }
        });

        FloatingActionButton doneFab = findViewById(R.id.done_fab);
        doneFab.setOnClickListener(view -> {
            if (selectedTag.name.equalsIgnoreCase(targetName)) {
                //TODO: remove makeText
                Toast.makeText(this, "Success: " + selectedTag.name, Toast.LENGTH_SHORT).show();
                //TODO: startActivity(new Intent(this, Successful.class));
                //TODO: finish() denk ik
            } else {
                startActivity(new Intent(this, SearchFailureActivity.class)
                        .putExtra(SearchFailureActivity.TARGET_TAG, targetName)
                        .putExtra(SearchFailureActivity.SELECTED_TAG, selectedTag.name));
                finish();
            }
        });

        setSelectedTag(null);
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
        if (pair == null) return;
        // als tag zit in de lijst, maar onder confidence_threshold -> close
        // als confidence >= confidence_threshold
        List<Tag> tags = pair.second;

        if (tags != null) {
            setIsLoading(false);
            List<Tag> options = TagUtil.filter(tags, Constants.MIN_TAG_CONFIDENCE_SEARCH);
            setTags(options);
            updateProgress(tags);
        }
    }

    private void updateProgress(List<Tag> tags) {
        for (Tag t : tags) {
            if (t.name.equalsIgnoreCase(targetName)) {
                Log.w(TAG, "Confidence in "  + targetName + " is: " + t.confidence);
                if (t.confidence >= Constants.MIN_CONFIDENCE_WARM)
                    updateIndicator(WARM);
                else if (t.confidence >= Constants.MIN_CONFIDENCE_NEUTRAL)
                    updateIndicator(NEUTRAL);
                else updateIndicator(COLD);
                return;
            }
        }
        updateIndicator(COLD);
    }

    private void updateIndicator(int status) {
        switch (status) {
            case WARM:
                toolbar.setBackgroundResource(R.color.color_temp_warm);
                break;
            case NEUTRAL:
                toolbar.setBackgroundResource(R.color.color_temp_neutral);                
                break;
            case COLD:
                toolbar.setBackgroundResource(R.color.color_temp_cold);
                break;
        }
    }

    private void setTags(List<Tag> tags) {
        TagUtil.sort(tags);

        tagsLayout.removeAllViews();

        for (Tag tag : tags) {
            View tagView = LayoutInflater.from(this).inflate(R.layout.tag_view, tagsLayout,
                    false);

            TextView tagName = tagView.findViewById(R.id.tag_name);
            tagName.setText(TextUtil.capitalizeFirstLetter(tag.name));

            tagName.setOnClickListener(view -> setSelectedTag(tag));

            tagsLayout.addView(tagView);
        }

    }

    private void setSelectedTag(Tag tag) {
        selectedTag = tag;

        if (tag != null) {
            selectedTagNameTextView.setText(TextUtil.capitalizeFirstLetter(tag.name));
        }

        selectedTagLayout.setVisibility(tag == null ? View.GONE : View.VISIBLE);
    }

    private void setIsLoading(boolean loading) {
        loadingText.setVisibility(loading ? View.VISIBLE : View.GONE);
    }
}

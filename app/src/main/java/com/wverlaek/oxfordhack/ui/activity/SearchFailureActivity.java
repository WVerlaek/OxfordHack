package com.wverlaek.oxfordhack.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wverlaek.oxfordhack.R;
import com.wverlaek.oxfordhack.util.TextUtil;

public class SearchFailureActivity extends AppCompatActivity {
    public static final String SELECTED_TAG = "FAIL_SELECTED_TAG";
    public static final String TARGET_TAG = "FAIL_TARGET_TAG";
    public static final String TARGET_ID = "FAIL_TARGET_ID";

    private String targetName;
    private int targetID;

    private View tryAgainButton;
    private TextView tagCorrectView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_failure);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(TARGET_TAG) || !intent.hasExtra(SELECTED_TAG) || !intent.hasExtra(TARGET_ID)) {
            finish();
            return;
        }

        String tagName = intent.getStringExtra(SELECTED_TAG);
        targetName = intent.getStringExtra(TARGET_TAG);
        targetID = intent.getIntExtra(TARGET_ID, -1);

        setTitle("");

        tryAgainButton = findViewById(R.id.try_again);
        tryAgainButton.setOnClickListener(view -> {
            startActivity(new Intent(this, SearchActivity.class)
                    .putExtra(SearchActivity.TARGET_ID, targetID)
                    .putExtra(SearchActivity.TARGET_TAG, targetName));
            finish();
        });

        tagCorrectView = findViewById(R.id.tag_correct);
        tagCorrectView.setText(TextUtil.capitalizeFirstLetter(tagName));

        View mainMenuButton = findViewById(R.id.main_menu_button);
        mainMenuButton.setOnClickListener(view -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}

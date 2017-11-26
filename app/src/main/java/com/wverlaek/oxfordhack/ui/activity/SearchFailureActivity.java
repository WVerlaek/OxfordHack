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
    public static final String TARGET_TAG = "SEARCH_TARGET_TAG";

    private ImageView snapshot;
    private TextView tagCorrectView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_failure);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(TARGET_TAG)) return;

        String tagName = intent.getStringExtra(TARGET_TAG);

        setTitle("");

        snapshot = findViewById(R.id.snapshot);
        snapshot.setOnClickListener(view -> {
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

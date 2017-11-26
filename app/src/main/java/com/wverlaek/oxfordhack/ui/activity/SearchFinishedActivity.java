package com.wverlaek.oxfordhack.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.projectoxford.vision.contract.Tag;
import com.wverlaek.oxfordhack.R;
import com.wverlaek.oxfordhack.serverapi.GetPictureListener;
import com.wverlaek.oxfordhack.serverapi.ServerAPI;
import com.wverlaek.oxfordhack.util.TextUtil;
import com.wverlaek.oxfordhack.vision.Picture;

import java.util.Arrays;

public class SearchFinishedActivity extends AppCompatActivity {
    public static final String TARGET_TAG = "SEARCH_SUCCESS_TAG";
    public static final String TARGET_ID = "SEARCH_TARGET_ID";

    private String targetName;
    private int targetID;

    private ImageView snapshot;
    private TextView tagCorrectView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(TARGET_TAG) || !intent.hasExtra(TARGET_ID)) return;

        targetName = intent.getStringExtra(TARGET_TAG);
        targetID = intent.getIntExtra(TARGET_ID, -1);

        setContentView(R.layout.activity_search_finished);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("");

        snapshot = findViewById(R.id.snapshot);

        tagCorrectView = findViewById(R.id.tag_correct);
        tagCorrectView.setText(TextUtil.capitalizeFirstLetter(targetName));

        View mainMenuButton = findViewById(R.id.main_menu_button);
        mainMenuButton.setOnClickListener(view -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        new ServerAPI().getPictureAsync(targetID,this, new GetPictureListener() {
            @Override
            public void onResult(Picture result) {
//                Toast.makeText(SearchFinishedActivity.this, "Loaded",
//                        Toast.LENGTH_SHORT).show();
                snapshot.setImageBitmap(result.getBitmap());
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(SearchFinishedActivity.this, "Unable to load picture",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


}
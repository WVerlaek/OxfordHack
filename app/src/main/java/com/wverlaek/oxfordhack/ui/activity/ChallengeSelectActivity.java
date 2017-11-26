package com.wverlaek.oxfordhack.ui.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.ActionBar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.wverlaek.oxfordhack.R;
import com.wverlaek.oxfordhack.serverapi.Challenge;
import com.wverlaek.oxfordhack.serverapi.GetResultListener;
import com.wverlaek.oxfordhack.serverapi.IServerAPI;
import com.wverlaek.oxfordhack.serverapi.ServerAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChallengeSelectActivity extends AppCompatActivity {
    // store as static, for demo purposes. This gets reset when app is restarted
    private static boolean showHints = true;

    private IServerAPI serverAPI = new ServerAPI();

    private List<Challenge> challengeList = new ArrayList<>();

    private ListView simpleList;
    private ArrayAdapter adapter;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_select);

        setTitle("Choose a challenge");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        simpleList = findViewById(R.id.challengeSelectListView);

        adapter = new ArrayAdapter<>(this, R.layout.challenge_item,
                R.id.challengeTextView, challengeList);
        simpleList.setAdapter(adapter);

        simpleList.setOnItemClickListener((parent, view, position, id) -> {
            Challenge challenge = challengeList.get(position);
            startChallenge(challenge);
        });

        // load challenges
        serverAPI.getChallengesAsync(this, new GetResultListener() {
            @Override
            public void onResult(List<Challenge> result) {
                challengeList.clear();
                challengeList.addAll(result);
                adapter.notifyDataSetChanged();

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                new AlertDialog.Builder(ChallengeSelectActivity.this)
                        .setTitle("Error")
                        .setMessage("Failed to load challenges. Are you connected to the internet?")
                        .setPositiveButton("close", null)
                        .show();

                progressBar.setVisibility(View.GONE);
            }
        });

        progressBar = findViewById(R.id.loading_bar);
    }

    private void startChallenge(Challenge challenge) {
        if (showHints) {
            new AlertDialog.Builder(this)
                    .setTitle("Start challenge")
                    .setMessage("Find the object in your area.\n\nYou will see a color change when you are getting close.\n\nMake your selection when you are confident you know which object is searched for.")
                    .setPositiveButton("Start", (dialog, which) -> {
                        startActivity(new Intent(this, SearchActivity.class)
                                .putExtra(SearchActivity.TARGET_TAG, challenge.tag)
                                .putExtra(SearchActivity.TARGET_ID, challenge.id));
                        finish();
                    })
                    .show();

            showHints = false;
        } else {
            startActivity(new Intent(this, SearchActivity.class)
                    .putExtra(SearchActivity.TARGET_TAG, challenge.tag)
                    .putExtra(SearchActivity.TARGET_ID, challenge.id));
            finish();
        }
    }
}

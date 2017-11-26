package com.wverlaek.oxfordhack.ui.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.ActionBar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.wverlaek.oxfordhack.R;
import com.wverlaek.oxfordhack.serverapi.Challenge;
import com.wverlaek.oxfordhack.serverapi.GetResultListener;
import com.wverlaek.oxfordhack.serverapi.IServerAPI;
import com.wverlaek.oxfordhack.serverapi.ServerAPI;

import java.util.ArrayList;
import java.util.List;

public class ChallengeSelectActivity extends AppCompatActivity {
    private static boolean showHints = true;
    ListView simpleList;

    IServerAPI serverAPI = new ServerAPI();

    List<Challenge> challengeList = new ArrayList<>();

    ArrayAdapter adapter;

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

        simpleList = (ListView) findViewById(R.id.challengeSelectListView);
        adapter = new ArrayAdapter<Challenge>(this, R.layout.challenge_item,
                R.id.challengeTextView, challengeList);
        simpleList.setAdapter(adapter);

        simpleList.setOnItemClickListener((parent, view, position, id) -> {
            Challenge t = challengeList.get(position);
            startActivity(new Intent(this, SearchActivity.class).putExtra(SearchActivity.TARGET_TAG, t.tag));
            finish();
        });

        serverAPI.getChallengesAsync(this, new GetResultListener() {
            @Override
            public void onResult(List<Challenge> result) {
                challengeList.addAll(result);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e) {
                new AlertDialog.Builder(ChallengeSelectActivity.this)
                        .setTitle("Error")
                        .setMessage(e.getMessage())
                        .setPositiveButton("close", null)
                        .setOnDismissListener(dialogInterface -> finish())
                        .show();
            }
        });

        if (showHints) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Tips")
                    .setMessage("1. Select one of the listed challenges.\n" +
                            "2. Point the camera at objects relating to the challenge.\n" +
                            "3. The app changes color to indicate you are getting close.\n" +
                            "4. Select from the list which object you think it is.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
//                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            showHints = false;
        }
    }
}

package com.wverlaek.oxfordhack.ui.activity;

import android.support.v7.app.AlertDialog;
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
    ListView simpleList;

    IServerAPI serverAPI = new ServerAPI();

    ArrayList<Challenge> challengeList = new ArrayList<>();

    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_select);
        simpleList = (ListView) findViewById(R.id.challengeSelectListView);
        adapter = new ArrayAdapter<Challenge>(this, R.layout.challenge_item,
                R.id.challengeTextView, challengeList);
        simpleList.setAdapter(adapter);

        simpleList.setOnItemClickListener((parent, view, position, id) -> {

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
    }
}

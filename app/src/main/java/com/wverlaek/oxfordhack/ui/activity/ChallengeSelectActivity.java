package com.wverlaek.oxfordhack.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.wverlaek.oxfordhack.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChallengeSelectActivity extends AppCompatActivity {
    ListView simpleList;

    ArrayList<String> testList = new ArrayList<>(Arrays.asList("Something black", "Something blue", "Something pink"));
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_select);
        simpleList = (ListView) findViewById(R.id.challengeSelectListView);
        adapter = new ArrayAdapter<String>(this, R.layout.activity_challenge,
                R.id.challengeTextView, testList);
        simpleList.setAdapter(adapter);

        simpleList.setOnItemClickListener((parent, view, position, id) -> {
            testList.add("Test..");
            adapter.notifyDataSetChanged();
        });
    }
}

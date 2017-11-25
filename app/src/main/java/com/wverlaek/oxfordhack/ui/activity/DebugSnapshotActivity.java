package com.wverlaek.oxfordhack.ui.activity;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.projectoxford.vision.contract.Tag;
import com.wverlaek.oxfordhack.R;
import com.wverlaek.oxfordhack.vision.IVisionAPI;
import com.wverlaek.oxfordhack.vision.Picture;
import com.wverlaek.oxfordhack.vision.ResultListener;
import com.wverlaek.oxfordhack.vision.VisionAPI;
import com.wverlaek.oxfordhack.vision.VisionResult;

import java.util.List;

public class DebugSnapshotActivity extends AppCompatActivity {

    private static Picture picture = null;

    private ImageView snapshot;
    private TextView tagsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_snapshot);

        if (picture == null) {
            finish();
        }

        snapshot = findViewById(R.id.snapshot);
        snapshot.setImageBitmap(picture.getBitmap());

        tagsTextView = findViewById(R.id.tags);
        tagsTextView.setText("Loading tags...");

        IVisionAPI api = new VisionAPI();
        api.getTagsAsync(this, picture, new ResultListener() {
            @Override
            public void onResult(VisionResult result) {
                List<Tag> tags = result.getTags();
                StringBuilder sb = new StringBuilder();
                for (Tag tag : tags) {
                    sb.append(tag.name).append(", ");
                }
                tagsTextView.setText(sb.toString());
            }

            @Override
            public void onError() {
                Toast.makeText(DebugSnapshotActivity.this, "Error loading results from API", Toast.LENGTH_SHORT).show();
                tagsTextView.setText("Error...");
            }
        });
    }

    public static void setPicture(Picture picture) {
        DebugSnapshotActivity.picture = picture;
    }
}

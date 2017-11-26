package com.wverlaek.oxfordhack.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wverlaek.oxfordhack.R;
import com.wverlaek.oxfordhack.serverapi.Challenge;
import com.wverlaek.oxfordhack.serverapi.PostResultListener;
import com.wverlaek.oxfordhack.serverapi.ServerAPI;
import com.wverlaek.oxfordhack.util.TextUtil;
import com.wverlaek.oxfordhack.vision.Picture;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ConfirmUploadActivity extends AppCompatActivity {

    private static String tag;
    private static Picture picture;

    private EditText nameEditText;
    private View uploadButton;
    private TextView tagTextView;
    private ImageView imageView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comfirm_upload);

        setTitle("Upload challenge");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (tag == null || picture == null) {
            Toast.makeText(this, "No tag or picture given.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        uploadButton = findViewById(R.id.confirm_button);
        nameEditText = findViewById(R.id.name_input);
        tagTextView = findViewById(R.id.tag_selected);
        imageView = findViewById(R.id.snapshot_taken);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        imageView.setImageBitmap(picture.getBitmap());
        tagTextView.setText(TextUtil.capitalizeFirstLetter(tag));
        nameEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        uploadButton.setOnClickListener(v -> {
            attemptStartUpload();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            promptDiscard();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void attemptStartUpload() {
        String name = nameEditText.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Give your challenge a name!", Toast.LENGTH_SHORT).show();
            return;
        }

        uploadButton.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        ServerAPI serverAPI = new ServerAPI();
        serverAPI.postChallengeAsync(this, new Challenge(name, tag, picture.getJpegData()), new PostResultListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(ConfirmUploadActivity.this, "Challenge uploaded!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(Exception e) {
                uploadButton.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ConfirmUploadActivity.this, "Error uploading challenge, please check your internet connection and try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        promptDiscard();
    }

    private void promptDiscard() {
        new AlertDialog.Builder(this)
                .setTitle("Discard challenge?")
                .setPositiveButton("Discard", (dialog, which) -> finish())
                .setNegativeButton("Cancel", null)
                .show();
    }

    public static void setTag(String tag) {
        ConfirmUploadActivity.tag = tag;
    }

    public static void setPicture(Picture picture) {
        ConfirmUploadActivity.picture = picture;
    }
}


package com.wverlaek.oxfordhack.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.projectoxford.vision.contract.Tag;
import com.wverlaek.oxfordhack.R;
import com.wverlaek.oxfordhack.ui.view.CameraPreview;
import com.wverlaek.oxfordhack.util.TextUtil;
import com.wverlaek.oxfordhack.vision.Picture;
import com.wverlaek.oxfordhack.vision.ResultListener;
import com.wverlaek.oxfordhack.vision.TagDetector;
import com.wverlaek.oxfordhack.vision.VisionResult;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private FrameLayout previewLayout;

    private int permissionRequestCode = 123;

    private Button snapshotButton;

    private TextView tagsTextView;

    private TagDetector tagDetector;

    private static final int HAS_PERMISSION = 0;
    private static final int PERMISSION_REQUEST_STARTED = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        previewLayout = findViewById(R.id.camera);
        snapshotButton = findViewById(R.id.snapshot_button);
        tagsTextView = findViewById(R.id.tags);

        tagDetector = new TagDetector(this, previewLayout);

        checkCameraPermission();

        tagDetector.getDetectedTags().observe(this, this::handleResult);
        tagDetector.getError().observe(this, throwable -> {
            if (throwable != null) {
                Log.e(TAG, "Error in tag detector", throwable);
                tagsTextView.setBackgroundColor(Color.RED);
            } else {
                tagsTextView.setBackgroundColor(Color.WHITE);
            }
        });
    }

    private void checkCameraPermission() {
        int status = checkOrRequestPermission();
        if (status == HAS_PERMISSION) {
            // start tag detector
            tagDetector.resume(this);
        } // else request started, we will get result later
    }

    @Override
    protected void onPause() {
        super.onPause();

        tagDetector.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (hasPermission()) {
            tagDetector.resume(this);
        }
    }

    private void handleResult(@Nullable List<Tag> tags) {
        String text = "";
        if (tags == null) {
            text = "Loading...";
        } else if (tags.isEmpty()) {
            text = "No tags found!";
        } else {
            StringBuilder sb = new StringBuilder();
            for (Tag tag : tags) {
                sb.append(tag.name).append(" ").append(TextUtil.formatDouble(tag.confidence)).append(", ");
            }
            text = sb.toString();
        }

        tagsTextView.setText(text);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == permissionRequestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tagDetector.resume(this);
            } else {
                showError("Please give the camera permission for this app to work.", dialogInterface -> finish());
            }
        }
    }



    private void showError(String msg, DialogInterface.OnDismissListener dismissListener) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(msg)
                .setPositiveButton("Close", null)
                .setOnDismissListener(dismissListener)
                .show();
    }

    private int checkOrRequestPermission() {
        if (!hasPermission()) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, permissionRequestCode);
            return PERMISSION_REQUEST_STARTED;
        } else {
            return HAS_PERMISSION;
        }
    }

    private boolean hasPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

}

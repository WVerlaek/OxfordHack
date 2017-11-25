package com.wverlaek.oxfordhack.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;

import com.wverlaek.oxfordhack.R;
import com.wverlaek.oxfordhack.ui.view.CameraPreview;
import com.wverlaek.oxfordhack.vision.Picture;

public class MainActivity extends AppCompatActivity {

    private FrameLayout previewLayout;

    private int permissionRequestCode = 123;

    private Button snapshotButton;
    private Camera camera;
    private CameraPreview preview;

    private static final int HAS_PERMISSION = 0;
    private static final int PERMISSION_REQUEST_STARTED = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        previewLayout = findViewById(R.id.camera);
        snapshotButton = findViewById(R.id.snapshot_button);

        tryGetCamera();

        snapshotButton.setOnClickListener(view -> takeSnapshot());
    }

    private void tryGetCamera() {
        int status = checkOrRequestPermission();
        if (status == HAS_PERMISSION) {
            camera = getCameraInstance();
            if (camera == null) {
                showError("Could not find camera. Is it in use, or unavailable?",
                        dialogInterface -> finish());
            } else {
                onCameraLoaded(camera);
            }
        } // else request started, we will get result later
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tryGetCamera();
    }

    private void onCameraLoaded(Camera camera) {
        camera.setDisplayOrientation(90);
        preview = new CameraPreview(this, camera);
        previewLayout.removeAllViews();
        previewLayout.addView(preview);
    }

    private void takeSnapshot() {
        camera.takePicture(null, null, (bytes, camera1) -> {
            DebugSnapshotActivity.setPicture(new Picture(bytes));
            startActivity(new Intent(MainActivity.this, DebugSnapshotActivity.class));
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == permissionRequestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onCameraLoaded(getCameraInstance());
            } else {
                showError("Please give the camera permission for this app to work.", dialogInterface -> finish());

            }
        }
    }

    @Nullable
    private static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            // not available or does not exist
        }
        return c;
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, permissionRequestCode);
            return PERMISSION_REQUEST_STARTED;
        } else {
            return HAS_PERMISSION;
        }
    }
}

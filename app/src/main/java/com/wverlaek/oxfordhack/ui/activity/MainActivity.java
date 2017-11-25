package com.wverlaek.oxfordhack.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.wverlaek.oxfordhack.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private int permissionRequestCode = 123;

    private static final int HAS_PERMISSION = 0;
    private static final int PERMISSION_REQUEST_STARTED = 1;

    private Button createChallengeButton;
    private Button solveChallengeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createChallengeButton = findViewById(R.id.create_challenge);
        createChallengeButton.setOnClickListener(view -> {
            // start select mode activity
            startActivity(new Intent(this, SelectActivity.class));
        });

        solveChallengeButton = findViewById(R.id.solve_challenge);
        solveChallengeButton.setOnClickListener(view -> {
            startActivity(new Intent(this, ChallengeSelectActivity.class));
        });

        // request camera permission if needed
        checkOrRequestPermission();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == permissionRequestCode) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                // close app after error message
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

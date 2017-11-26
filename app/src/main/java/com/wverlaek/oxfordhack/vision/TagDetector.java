package com.wverlaek.oxfordhack.vision;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;
import android.view.ViewGroup;

import com.microsoft.projectoxford.vision.contract.Tag;
import com.wverlaek.oxfordhack.ui.activity.MainActivity;
import com.wverlaek.oxfordhack.ui.view.CameraPreview;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by s148327 on 25-11-2017.
 */

public class TagDetector {
    private static final String TAG = "TagDetector";

    private ViewGroup container;

    private Camera camera;
    private CameraPreview preview;

    private Handler handler;

    private VisionAPI visionAPI;

    private MutableLiveData<Pair<Picture, List<Tag>>> mutableTags = new MutableLiveData<>();
    private MutableLiveData<Throwable> mutableError = new MutableLiveData<>();

    private Timer timer = null;
    private static final int POLL_RATE = 2000; // 4 sec

    private boolean takingPicture = false;

    public TagDetector(Context context, ViewGroup container) {
        this.container = container;
        mutableTags.setValue(null);
        mutableError.setValue(null);

        handler = new Handler(Looper.getMainLooper());
        visionAPI = new VisionAPI();
    }

    public void pause() {
        if (camera != null) {
//            preview.getHolder().removeCallback(preview);
//            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
        }

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void resume(Context context) {
        camera = getCameraInstance();

        preview = new CameraPreview(context, camera);
        container.removeAllViews();
        container.addView(preview);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                doAnalyze(context);
            }
        }, 500, POLL_RATE);
    }

    public LiveData<Pair<Picture, List<Tag>>> getDetectedTags() {
        return mutableTags;
    }

    public LiveData<Throwable> getError() {
        return mutableError;
    }

    private void doAnalyze(Context context) {
        takePicture(picture -> {
            if (picture != null) {
                visionAPI.getTagsAsync(context, picture, new ResultListener() {
                    @Override
                    public void onResult(VisionResult result) {
                        mutableTags.setValue(Pair.create(picture, result.getTags()));
                        mutableError.setValue(null);
                    }

                    @Override
                    public void onError(Exception e) {
                        mutableError.setValue(e);
                    }
                });
            }
        });
    }

    private void takePicture(OnPictureListener listener) {
        if (takingPicture) {
            return;
        }
        takingPicture = true;
        Picture picture = preview.getPreviewPicture();
        takingPicture = false;

        listener.onPicture(picture);

//        camera.takePicture(null, null, (bytes, camera1) -> {
//            takingPicture = false;
//            listener.onPicture(new Picture(bytes));
//            camera.stopPreview();
//            camera.startPreview();
//        });
    }

    @Nullable
    private static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
            c.setDisplayOrientation(90);
        } catch (Exception e) {
            // not available or does not exist
            Log.e(TAG, "Error loading camera", e);
        }
        return c;
    }

    @FunctionalInterface
    public interface OnPictureListener {
        void onPicture(@Nullable Picture picture);
    }
}

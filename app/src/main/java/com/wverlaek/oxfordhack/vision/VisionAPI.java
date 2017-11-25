package com.wverlaek.oxfordhack.vision;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;
import com.wverlaek.oxfordhack.util.Constants;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by notor on 11/25/2017.
 */

public class VisionAPI implements IVisionAPI {
    private static final String TAG = "VisionAPI";

    private final String apiRoot = "https://westcentralus.api.cognitive.microsoft.com/vision/v1.0";

    private final String[] visualFeatures = new String[] {"Tags"};
    private final String[] details = new String[] {};

    private VisionServiceClient client;

    public VisionAPI() {
        client = new VisionServiceRestClient(Constants.MS_VISION_API_KEY, Constants.MS_VISION_ENDPOINT);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getTagsAsync(Context context, final Picture picture, final ResultListener listener) {
        new AsyncTask<Picture, Void, VisionResult>() {

            private Exception exception = null;

            @Override
            protected VisionResult doInBackground(Picture... pictures) {
                Picture pic = pictures[0];

                try {
                    return new VisionResult(client.analyzeImage(new ByteArrayInputStream(pic.getJpegData()), visualFeatures, details).tags);
                } catch (VisionServiceException vse) {
                    exception = vse;
                    Log.e(TAG, "Error while analyzing image", vse);
                } catch (IOException e) {
                    exception = e;
                    Log.e(TAG, "Error while analyzing image", e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(VisionResult visionResult) {
                if (visionResult == null) {
                    listener.onError(exception);
                } else {
                    listener.onResult(visionResult);
                }
            }
        }.execute(picture);
    }
}

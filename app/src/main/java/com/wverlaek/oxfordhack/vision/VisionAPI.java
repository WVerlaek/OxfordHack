package com.wverlaek.oxfordhack.vision;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by notor on 11/25/2017.
 */

public class VisionAPI implements IVisionAPI {
    private static final String TAG = "VisionAPI";

    private final String uriBase = "https://westcentralus.api.cognitive.microsoft.com/vision/v1.0/analyze";
    private final String apiRoot = "https://westcentralus.api.cognitive.microsoft.com/vision/v1.0";
    private final String subscriptionKey = "5a7cce02d6b34eeab0fd0f7fdae416bb";//"f89c7c62e1ca48fd93e78c9191308a3a";
    private final String[] visualFeatures = new String[] {"Tags"};
    private final String[] details = new String[] {};

    private VisionServiceClient client;

    public VisionAPI() {
        client = new VisionServiceRestClient(subscriptionKey, apiRoot);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getTagsAsync(Context context, final Picture picture, final ResultListener listener) {
        new AsyncTask<Picture, Void, VisionResult>() {
//            OkHttpClient client = new OkHttpClient();

            @Override
            protected VisionResult doInBackground(Picture... pictures) {
                Picture pic = pictures[0];

                try {
                    return new VisionResult(client.analyzeImage(new ByteArrayInputStream(pic.getData()), visualFeatures, details).tags);
                } catch (VisionServiceException vse) {
                    Log.e(TAG, "Error while analyzing image", vse);
                } catch (IOException e) {
                    Log.e(TAG, "Error while analyzing image", e);
                }
                return null;

//                final MediaType MEDIA_TYPE = MediaType.parse("image/jpeg");
//
//                String visualFeatures = "?visualFeatures=Tags";
//
//                RequestBody body = RequestBody.create(MEDIA_TYPE, picture.getData());
//                Request request = new Request.Builder()
//                        .url(uriBase + visualFeatures)
//                        .addHeader("Content-Type", "application/octet-stream")
//                        .addHeader("Ocp-Apim-Subscription-Key", subscriptionKey)
//                        .post(body)
//                        .build();
//                try {
//                    Response response = client.newCall(request).execute();
//                    if (response.code() != 200) {
//                        Log.e("VisionAPI", "Invalid response code: " + response.code() + ". Message: " + response.message());
//                        return null;
//                    }
//                    String jsonString = response.body().string();
//                    Gson gson = new Gson();
//                    VisionAPIResponse apiResponse = gson.fromJson(jsonString, VisionAPIResponse.class);
//
////                    List<Tag> tags = new ArrayList<>();
////                    for (String tag : apiResponse.description.tags) {
////                        tags.add(new Tag(tag));
////                    }
//                    return new VisionResult(apiResponse.tags);
//
//                } catch (IOException ioE)  {
//                    Log.e("VisionAPI", "API error", ioE);
//                    return null;
//                }
            }

            @Override
            protected void onPostExecute(VisionResult visionResult) {
                if (visionResult == null) {
                    listener.onError();
                } else {
                    listener.onResult(visionResult);
                }
            }
        }.execute(picture);
    }
}

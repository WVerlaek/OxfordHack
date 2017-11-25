package com.wverlaek.oxfordhack.vision;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by notor on 11/25/2017.
 */

public class VisionAPI implements IVisionAPI {

    private final String uriBase = "https://westcentralus.api.cognitive.microsoft.com/vision/v1.0/analyze";
    private final String subscriptionKey = "f89c7c62e1ca48fd93e78c9191308a3a";

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getTagsAsync(Context context, final Picture picture, final ResultListener listener) {
        new AsyncTask<Picture, Void, VisionResult>() {


            OkHttpClient client = new OkHttpClient();

            @Override
            protected VisionResult doInBackground(Picture... pictures) {
                final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
                RequestBody body = RequestBody.create(MEDIA_TYPE_PNG, picture.getData());
                Request request = new Request.Builder()
                        .url(uriBase)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Ocp-Apim-Subscription-Key", subscriptionKey)
                        .post(body)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.code() != 200) {
                        return null;
                    }
                    String jsonString = response.body().string();
                    Gson gson = new Gson();
                    VisionAPIResponse apiResponse = gson.fromJson(jsonString, VisionAPIResponse.class);

                    return new VisionResult(apiResponse.description.tags);

                } catch (IOException ioE)  {
                    Log.e("VisionAPI", "API error", ioE);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(VisionResult visionResult) {
                listener.onResult(visionResult);
            }
        }.execute(picture);
    }
}

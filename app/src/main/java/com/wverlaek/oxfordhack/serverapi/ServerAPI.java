package com.wverlaek.oxfordhack.serverapi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wverlaek.oxfordhack.util.TextUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by notor on 11/25/2017.
 */

public class ServerAPI implements IServerAPI {
    private final String getUrl = "http://178.62.27.128/get/all";
    private final String postUrl = "http://178.62.27.128/upload";

    OkHttpClient client = new OkHttpClient();

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getChallengesAsync(Context context, GetResultListener listener) {
        new AsyncTask<Void, Void, List<Challenge>>() {
            private Exception exception = null;

            @Override
            protected List<Challenge> doInBackground(Void... voids) {
                Request request = new Request.Builder()
                        .url(getUrl)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response != null && response.code() == 200 && response.body() != null) {
                        Gson gson = new Gson();
                        return gson.fromJson(response.body().string(),
                                new TypeToken<List<Challenge>>(){}.getType());
                    } else {
                        throw new IOException("Reponse code " + response.code() + " msg " +
                            response.message());
                    }
                } catch (IOException ioE) {
                    exception = ioE;
                    Log.e("ServerAPI", "Get request failed", ioE);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<Challenge> result) {
                if (result == null) {
                    listener.onError(exception);
                } else {
                    listener.onResult(result);
                }
            }
        }.execute();
    }



    @SuppressLint("StaticFieldLeak")
    @Override
    public void postChallengeAsync(Context context, Challenge challenge, PostResultListener listener) {
        new AsyncTask<Challenge, Void, Void>() {
            private Exception exception = null;

            @Override
            protected Void doInBackground(Challenge... challenges) {
                Challenge challenge = challenges[0];

                MediaType MEDIA_TYPE_PLAINTEXT = MediaType
                        .parse("text/plain; charset=utf-8");

                RequestBody formBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file", "newfile.jpeg",
                                RequestBody.create(MEDIA_TYPE_PLAINTEXT, challenge.file))
                        .addFormDataPart("name", challenge.name)
                        .addFormDataPart("tags", challenge.tag)
                        .build();

                Request request = new Request.Builder()
                        .url(postUrl)
                        .post(formBody)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful() || response.code() != 200) throw new IOException("Unexpected code " + response);
                    return null;
                } catch (IOException ioE) {
                    exception = ioE;
                    Log.e("ServerAPI", "Post request failed", ioE);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (exception != null) {
                    listener.onError(exception);
                } else {
                    listener.onSuccess();
                }
            }
        }.execute(challenge);
    }
}

package com.wverlaek.oxfordhack.serverapi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.wverlaek.oxfordhack.ui.activity.ChallengeSelectActivity;

import java.io.IOException;
import java.io.SyncFailedException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by notor on 11/25/2017.
 */

public class ServerAPI implements IServerAPI {
    private final String url = "http://178.62.27.128/get/all";
    OkHttpClient client = new OkHttpClient();

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getChallengesAsync(Context context, GetResultListener listener) {
        new AsyncTask<Void, Void, List<Challenge>>() {
            private Exception exception = null;
            private String json = null;

            @Override
            protected List<Challenge> doInBackground(Void... voids) {
                Request request = new Request.Builder()
                        .url(url)
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
    public void postChallengeAsync(Context context, PostResultListener listener) {
        new AsyncTask<Challenge, Void, Void>() {

            @Override
            protected Void doInBackground(Challenge... challenges) {
                Challenge challenge = challenges[0];
                return null;
            }
        }.execute();
    }
}

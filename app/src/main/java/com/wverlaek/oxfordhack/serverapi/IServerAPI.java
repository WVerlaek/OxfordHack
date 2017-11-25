package com.wverlaek.oxfordhack.serverapi;

import android.content.Context;

/**
 * Created by notor on 11/25/2017.
 */

public interface IServerAPI {
    void getChallengesAsync(Context context, GetResultListener listener);

    void postChallengeAsync(Context context, Challenge challenge, PostResultListener listener);
}

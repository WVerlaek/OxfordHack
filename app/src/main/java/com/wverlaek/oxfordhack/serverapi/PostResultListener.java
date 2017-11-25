package com.wverlaek.oxfordhack.serverapi;

/**
 * Created by notor on 11/25/2017.
 */

public interface PostResultListener {
    void onSuccess();
    void onError(Exception e);
}

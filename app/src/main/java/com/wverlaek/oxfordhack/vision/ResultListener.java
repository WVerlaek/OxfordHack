package com.wverlaek.oxfordhack.vision;

/**
 * Created by s148327 on 25-11-2017.
 */

public interface ResultListener {
    void onResult(VisionResult result);
    void onError();
}

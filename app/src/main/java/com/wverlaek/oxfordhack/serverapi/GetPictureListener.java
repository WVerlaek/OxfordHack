package com.wverlaek.oxfordhack.serverapi;
import com.wverlaek.oxfordhack.vision.Picture;

/**
 * Created by notor on 11/25/2017.
 */

public interface GetPictureListener {
    void onResult(Picture result);
    void onError(Exception e);
}

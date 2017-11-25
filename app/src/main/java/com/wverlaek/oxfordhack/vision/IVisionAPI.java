package com.wverlaek.oxfordhack.vision;

/**
 * Created by s148327 on 25-11-2017.
 */

public interface IVisionAPI {

    void getTagsAsync(Picture picture, ResultListener listener);

}

package com.wverlaek.oxfordhack.vision;

import android.content.Context;

/**
 * Created by s148327 on 25-11-2017.
 */

public interface IVisionAPI {

    void getTagsAsync(Context context, Picture picture, ResultListener listener);

}

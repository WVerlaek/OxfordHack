package com.wverlaek.oxfordhack.serverapi;
import java.util.List;

/**
 * Created by notor on 11/25/2017.
 */

public interface GetResultListener {
    void onResult(List<Challenge> result);
    void onError(Exception e);
}

package com.wverlaek.oxfordhack.vision;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.lang.ref.WeakReference;

/**
 * Created by s148327 on 25-11-2017.
 */

public class Picture {
    private byte[] data;

    private WeakReference<Bitmap> bitmap = new WeakReference<>(null);

    public Picture(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public Bitmap getBitmap() {
        Bitmap b = bitmap.get();
        if (b == null) {
            // generate new
            b = BitmapFactory.decodeByteArray(data, 0, data.length);
            bitmap = new WeakReference<>(b);
        }
        return b;
    }
}

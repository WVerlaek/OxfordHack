package com.wverlaek.oxfordhack.vision;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;

/**
 * Created by s148327 on 25-11-2017.
 */

public class Picture {
    private byte[] data;

    private WeakReference<Bitmap> bitmap = new WeakReference<>(null);

    public Picture(byte[] data) {
        // generate new
        Bitmap b = BitmapFactory.decodeByteArray(data, 0, data.length);
        Matrix mtx = new Matrix();
        mtx.postRotate(90);
        // Rotating Bitmap
        b = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
                b.getHeight(), mtx, true);
        bitmap = new WeakReference<>(b);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 85, stream);
        this.data = stream.toByteArray();
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

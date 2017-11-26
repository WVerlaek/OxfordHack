package com.wverlaek.oxfordhack.vision;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by s148327 on 25-11-2017.
 */

public class Picture {
    private byte[] data;

    private WeakReference<Bitmap> bitmap = new WeakReference<>(null);

//    public Picture(InputStream byteStream) {
//        Bitmap b = BitmapFactory.decodeStream(byteStream);
//        bitmap = new WeakReference<Bitmap>(b);
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        b.compress(Bitmap.CompressFormat.JPEG, 85, stream);
//        this.data = stream.toByteArray();
//    }

    public  Picture(byte[] jpegData, boolean rotate) {
        if (rotate) {
            // generate new
            Bitmap b = BitmapFactory.decodeByteArray(jpegData, 0, jpegData.length);
            Matrix mtx = new Matrix();
            mtx.postRotate(90);
            // Rotating Bitmap
            b = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
                    b.getHeight(), mtx, true);
            bitmap = new WeakReference<>(b);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.JPEG, 85, stream);
            this.data = stream.toByteArray();
        } else {
            this.data = jpegData;
        }
    }

    public byte[] getJpegData() {
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

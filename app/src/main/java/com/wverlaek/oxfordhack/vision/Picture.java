package com.wverlaek.oxfordhack.vision;

/**
 * Created by s148327 on 25-11-2017.
 */

public class Picture {
    private byte[] data;

    public Picture(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }
}

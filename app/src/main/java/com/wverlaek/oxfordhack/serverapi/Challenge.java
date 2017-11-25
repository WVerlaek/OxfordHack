package com.wverlaek.oxfordhack.serverapi;

import com.microsoft.projectoxford.vision.contract.Tag;

import java.util.List;

/**
 * Created by notor on 11/25/2017.
 */

public class Challenge {
    public int id;
    public String name;
    public String tag;
    public byte[] file;

    public Challenge(String name, String tag, byte[] file) {
        this.name = name;
        this.tag = tag;
        this.file = file;
    }

    @Override
    public String toString() {
        return this.name;
    }
}





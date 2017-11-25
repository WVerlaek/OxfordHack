package com.wverlaek.oxfordhack.serverapi;

import com.microsoft.projectoxford.vision.contract.Tag;

import java.util.List;

/**
 * Created by notor on 11/25/2017.
 */

public class Challenge {
    public int id;
    public String name;
    public List<String> tags;
    public byte[] file;

    Challenge(int id, String name, List<String> tags, byte[] file) {
        this.id = id;
        this.name = name;
        this.tags = tags;
        this.file = file;
    }

    @Override
    public String toString() {
        return this.name;
    }
}





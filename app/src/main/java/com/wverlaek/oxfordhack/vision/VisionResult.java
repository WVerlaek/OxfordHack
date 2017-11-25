package com.wverlaek.oxfordhack.vision;

import com.microsoft.projectoxford.vision.contract.Tag;

import java.util.List;

/**
 * Created by s148327 on 25-11-2017.
 */

public class VisionResult {
    private List<Tag> tags;

    public VisionResult(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Tag> getTags() {
        return tags;
    }
}

package com.wverlaek.oxfordhack.util;

import com.microsoft.projectoxford.vision.contract.Tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TagUtil {
    // don't show these tags
    private static final List<String> ignoredTags = Arrays.asList("abstract", "indoor", "sitting", "blur");

    /**
     * Filters a list of tags on confidence level and returns the resulting list.
     */
    public static List<Tag> filter(List<Tag> tags, double minConfidence) {
        List<Tag> result = new ArrayList<>();
        if (tags != null) {
            for (Tag tag : tags) {
                if (ignoredTags.contains(tag.name)) {
                    continue;
                }
                if (tag.confidence >= minConfidence) {
                    result.add(tag);
                }
            }
        }
        return result;
    }

    /**
     * Sorts tags in decreasing order on confidence
     */
    public static void sort(List<Tag> tags) {
        Collections.sort(tags, (tag, t1) -> -Double.compare(tag.confidence, t1.confidence));
    }
}

package com.wverlaek.oxfordhack.util;

/**
 * Created by s148327 on 25-11-2017.
 */

public class Constants {

    public static final double MIN_TAG_CONFIDENCE_SELECT = 0.5;
    public static final double MIN_TAG_CONFIDENCE_SEARCH = 0.3;

    public static final double MIN_CONFIDENCE_NEUTRAL = 0.2;
    public static final double MIN_CONFIDENCE_WARM = 0.95;

    // Microsoft Vision API key
    public static final String MS_VISION_ENDPOINT = "https://westeurope.api.cognitive.microsoft.com/vision/v1.0"; //"https://westcentralus.api.cognitive.microsoft.com/vision/v1.0"
    public static final String MS_VISION_API_KEY = "a8dd8678ed8c41fabef3a2ff35765b31"; // premium key: "a8dd8678ed8c41fabef3a2ff35765b31"; // trial keys: "5a7cce02d6b34eeab0fd0f7fdae416bb"; //"f89c7c62e1ca48fd93e78c9191308a3a";
}

package com.wverlaek.oxfordhack.util;

import java.text.DecimalFormat;

/**
 * Created by s148327 on 25-11-2017.
 */

public class TextUtil {

    public static String formatDouble(double d) {
        return new DecimalFormat("#0.00").format(d);
    }

    public static String capitalizeFirstLetter(String s) {
        if (s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}

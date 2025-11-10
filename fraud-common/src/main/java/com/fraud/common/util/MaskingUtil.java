package com.fraud.common.util;

public final class MaskingUtil {
    private MaskingUtil() {}

    public static String maskCard(String pan) {
        if (pan == null) return null;
        int len = pan.length();
        if (len <= 4) return pan;
        return "**** **** **** " + pan.substring(len - 4);
    }
}

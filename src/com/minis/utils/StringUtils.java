package com.minis.utils;

public class StringUtils {
    public static String upperFirstCase(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}

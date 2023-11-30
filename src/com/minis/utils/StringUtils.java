package com.minis.utils;

public class StringUtils {
    public static String upperFirstCase(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static boolean isEmpty(String s) {
        return s == null || "".equals(s);
    }

    public static String trimAllWhitespace(String str) {
        if (!isEmpty(str)) {
            return str;
        }

        int len = str.length();
        StringBuilder sb = new StringBuilder(str.length());
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (!Character.isWhitespace(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}

package org.example.util;

import java.util.List;

public class Validate {

    public static String requireText(String text, String field) {
        if (text == null) throw new IllegalArgumentException(field + " cannot be null");
        String t = text.trim();
        if (t.isEmpty()) throw new IllegalArgumentException(field + " cannot be blank");
        return t;
    }

    public static int requirePositiveOrZero(int v, String field) {
        if (v < 0) throw new IllegalArgumentException(field + " must be >= 0");
        return v;
    }

    public static <T> void requireSizeBetween(List<T> list, int min, int max, String field) {
        if (list == null) throw new IllegalArgumentException(field + " cannot be null");
        int s = list.size();
        if (s < min || s > max) {
            throw new IllegalArgumentException(field + " size must be between " + min + " and " + max + " but was " + s);
        }
    }
}

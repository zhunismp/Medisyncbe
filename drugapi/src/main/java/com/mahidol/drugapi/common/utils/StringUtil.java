package com.mahidol.drugapi.common.utils;

import io.vavr.collection.Array;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringUtil {
    public static String arrayToString(List<String> array) {
        if (array == null || array.isEmpty()) return "";
        else return String.join(",", array);
    }

    public static List<String> stringToArray(String str) {
        if (str == null || str.isEmpty()) return Collections.emptyList();
        else return Arrays.stream(str.split(",")).toList();
    }
}

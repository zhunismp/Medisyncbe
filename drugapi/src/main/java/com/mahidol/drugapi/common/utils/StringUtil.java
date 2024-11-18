package com.mahidol.drugapi.common.utils;

import io.vavr.collection.Array;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StringUtil {
    public static <T> String arrayToString(List<T> array) {
        if (array == null || array.isEmpty()) return "";

        return array.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    public static List<String> stringToArray(String str) {
        if (str == null || str.isEmpty()) return Collections.emptyList();
        else return Arrays.stream(str.split(",")).toList();
    }
}

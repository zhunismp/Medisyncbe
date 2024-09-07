package com.mahidol.drugapi.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.vavr.control.Either;

import java.lang.reflect.Type;
import java.util.List;

public class JsonParser {
    private static final Gson gson = new GsonBuilder().serializeNulls().create();

    public static <T> Either<Exception, T> parseJson(String jsonString, Class<T> tClass) {
        try {
            return Either.right(gson.fromJson(jsonString, tClass));
        } catch (Exception e) {
            return Either.left(e);
        }

    }

    public static <T> Either<Exception, List<T>> parseJsonList(String jsonString, Class<T> tClass) {
        try {
            Type listType = TypeToken.getParameterized(List.class, tClass).getType();
            return Either.right(gson.fromJson(jsonString, listType));
        } catch (Exception e) {
            return Either.left(e);
        }

    }

    public static <T> Either<Exception, String> toJsonString(T object) {
        try {
            return Either.right(gson.toJson(object));
        } catch (Exception e) {
            return Either.left(e);
        }

    }
}

package com.mahidol.drugapi.user.models.types;

import java.util.Arrays;

public enum Gender {
    MALE('M'),
    FEMALE('F');

    private final Character value;

    Gender(Character value) {
        this.value = value;
    }

    @Override
    public String toString() { return value.toString(); }

    public static Gender fromValue(Character value) {
        return Arrays.stream(Gender.values()).filter(v -> v.value == value).findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid value of " + value));
    }
}

package com.mahidol.drugapi.user.models.types;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum BloodGroup {
    A_POSITIVE("A+"),
    A_NEGATIVE("A-"),
    B_POSITIVE("B+"),
    B_NEGATIVE("B-"),
    AB_POSITIVE("AB+"),
    AB_NEGATIVE("AB-"),
    O_POSITIVE("O+"),
    O_NEGATIVE("O-");

    private final String value;

    BloodGroup(String value) {
        this.value = value;
    }

    @Override
    public String toString() { return value; }

    public static BloodGroup fromValue(String value) {
        return Arrays.stream(BloodGroup.values()).filter(v -> v.value.equals(value)).findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid value of " + value));
    }
}

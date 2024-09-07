package com.mahidol.drugapi.drug.models.type;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum MealCondition {
    PRE_MEAL(1),
    DURING_MEAL(2),
    POST_MEAL(3);

    private final int value;

    MealCondition(int value) {
        this.value = value;
    }

    public static MealCondition fromValue(int value) {
        return Arrays.stream(MealCondition.values()).filter(v -> v.value == value).findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid value of " + value));
    }
}

package com.mahidol.drugapi.history.models.types;

import lombok.Getter;

@Getter
public enum TakenStatus {
    TAKEN("taken"),
    SKIPPED("skipped"),
    MISSED("missed");

    private final String value;

    TakenStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
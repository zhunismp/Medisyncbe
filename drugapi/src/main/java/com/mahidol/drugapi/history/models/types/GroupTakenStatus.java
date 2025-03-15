package com.mahidol.drugapi.history.models.types;

public enum GroupTakenStatus {
    ALL_TAKEN("all_taken"),
    PARTIALLY_TAKEN("partially_taken"),
    MISSED("missed");

    private final String value;

    GroupTakenStatus(String value) { this.value = value; }

    @Override
    public String toString() {
        return value;
    }
}

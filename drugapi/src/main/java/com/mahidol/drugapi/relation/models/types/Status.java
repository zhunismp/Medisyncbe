package com.mahidol.drugapi.relation.models.types;

import lombok.Getter;

@Getter
public enum Status {
    PENDING("pending"),
    ACCEPTED("accepted"),
    REJECTED("rejected");

    private final String value;

    Status(String value) { this.value = value; }

    @Override
    public String toString() { return value; }
}

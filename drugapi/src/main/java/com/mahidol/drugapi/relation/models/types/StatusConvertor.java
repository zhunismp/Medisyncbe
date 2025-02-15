package com.mahidol.drugapi.relation.models.types;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusConvertor implements AttributeConverter<Status, String> {
    @Override
    public String convertToDatabaseColumn(Status status) {
        return (status != null) ? status.toString() : null;
    }

    @Override
    public Status convertToEntityAttribute(String s) {
        return (s != null) ? Status.valueOf(s.toUpperCase()) : null;
    }
}

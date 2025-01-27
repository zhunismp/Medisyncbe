package com.mahidol.drugapi.history.models.types;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TakenStatusConvertor implements AttributeConverter<TakenStatus, String> {

    @Override
    public String convertToDatabaseColumn(TakenStatus status) {
        return (status != null) ? status.toString() : null;
    }

    @Override
    public TakenStatus convertToEntityAttribute(String dbData) {
        return (dbData != null) ? TakenStatus.valueOf(dbData.toUpperCase()) : null;
    }
}
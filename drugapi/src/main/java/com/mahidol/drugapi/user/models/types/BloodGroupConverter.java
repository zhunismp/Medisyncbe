package com.mahidol.drugapi.user.models.types;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BloodGroupConverter implements AttributeConverter<BloodGroup, String> {

    @Override
    public String convertToDatabaseColumn(BloodGroup bloodGroup) {
        return (bloodGroup != null) ? bloodGroup.toString().toUpperCase() : null;
    }

    @Override
    public BloodGroup convertToEntityAttribute(String dbData) {
        return (dbData != null) ? BloodGroup.fromValue(dbData) : null;
    }
}



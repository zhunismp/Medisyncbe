package com.mahidol.drugapi.user.models.types;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class GenderConverter implements AttributeConverter<Gender, Character> {

    @Override
    public Character convertToDatabaseColumn(Gender gender) {
        return (gender != null) ? gender.toString().charAt(0) : null;  // Convert Gender enum to 'M' or 'F'
    }

    @Override
    public Gender convertToEntityAttribute(Character dbData) {
        return (dbData != null) ? Gender.fromValue(dbData) : null;  // Convert 'M' or 'F' to Gender enum
    }
}

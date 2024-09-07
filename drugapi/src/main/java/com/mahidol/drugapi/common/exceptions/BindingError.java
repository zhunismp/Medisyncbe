package com.mahidol.drugapi.common.exceptions;

import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
public class BindingError extends RuntimeException {
    private final List<FieldError> messages;

    public BindingError(List<FieldError> messages) {
        super("Error from user request");
        this.messages = messages;
    }
}

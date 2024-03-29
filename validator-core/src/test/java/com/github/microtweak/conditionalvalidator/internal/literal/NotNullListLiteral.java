package com.github.microtweak.conditionalvalidator.internal.literal;

import jakarta.validation.constraints.NotNull;
import java.lang.annotation.Annotation;

public class NotNullListLiteral implements NotNull.List {

    private final NotNull[] value;

    public NotNullListLiteral(NotNull... value) {
        this.value = value;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return NotNull.List.class;
    }

    @Override
    public NotNull[] value() {
        return value;
    }

}

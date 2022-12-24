package com.github.microtweak.validator.conditional.internal.literal;

import javax.validation.constraints.NotEmpty;
import java.lang.annotation.Annotation;

public class NotEmptyListLiteral implements NotEmpty.List {

    private final NotEmpty[] value;

    public NotEmptyListLiteral(NotEmpty... value) {
        this.value = value;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return NotEmpty.List.class;
    }

    @Override
    public NotEmpty[] value() {
        return value;
    }

}

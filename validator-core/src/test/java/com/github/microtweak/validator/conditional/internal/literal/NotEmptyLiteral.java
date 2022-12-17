package com.github.microtweak.validator.conditional.internal.literal;

import javax.validation.Payload;
import javax.validation.constraints.NotEmpty;
import java.lang.annotation.Annotation;

public class NotEmptyLiteral implements NotEmpty {

    @Override
    public String message() {
        return "must not be empty";
    }

    @Override
    public Class<?>[] groups() {
        return new Class[0];
    }

    @Override
    public Class<? extends Payload>[] payload() {
        return new Class[0];
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return NotEmpty.class;
    }

    public static NotEmpty.List asList(NotEmpty... value) {
        return new NotEmpty.List() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return NotEmpty.List.class;
            }

            @Override
            public NotEmpty[] value() {
                return value;
            }
        };
    }

}

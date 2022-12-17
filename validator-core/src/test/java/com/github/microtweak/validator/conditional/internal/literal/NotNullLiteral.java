package com.github.microtweak.validator.conditional.internal.literal;

import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;

public class NotNullLiteral implements NotNull {

    @Override
    public String message() {
        return "must not be null";
    }

    @Override
    public Class<?>[] groups() {
        return new Class[0];
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends Payload>[] payload() {
        return new Class[0];
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return NotNull.class;
    }

    public static NotNull.List asList(NotNull... value) {
        return new NotNull.List() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return NotNull.List.class;
            }

            @Override
            public NotNull[] value() {
                return value;
            }
        };
    }

}

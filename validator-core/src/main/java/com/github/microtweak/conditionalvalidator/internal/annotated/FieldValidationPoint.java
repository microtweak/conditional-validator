package com.github.microtweak.conditionalvalidator.internal.annotated;

import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

@ToString
public class FieldValidationPoint implements ValidationPoint {

    private final Field field;

    @Getter
    private final List<Annotation> constraints;

    FieldValidationPoint(Field field, List<Annotation> constraints) {
        this.field = field;
        field.setAccessible(true);

        this.constraints = constraints;
    }

    @Override
    public String getName() {
        return field.getName();
    }

    @Override
    public Class<?> getDeclaringClass() {
        return field.getDeclaringClass();
    }

    @Override
    public Class<?> getType() {
        return field.getType();
    }

    @Override
    public Object getValidatedValue(Object bean) {
        try {
            return FieldUtils.readField(field, bean);
        } catch (IllegalAccessException e) {
            return ExceptionUtils.rethrow(e);
        }
    }
}

package com.github.microtweak.validator.conditional.core.internal.annotated;

import com.github.microtweak.validator.conditional.core.ValidateAs;
import com.github.microtweak.validator.conditional.core.internal.helper.AnnotationHelper;
import org.apache.commons.lang3.ObjectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.List;

import static java.lang.String.format;

public interface ValidationPoint {

    String getName();

    Class<?> getDeclaringClass();

    Class<?> getType();

    List<Annotation> getConstraints();

    Object getValidatedValue(Object bean);

    static ValidationPoint of(AnnotatedElement element) {
        final List<Annotation> constraints = AnnotationHelper.findAnnotationsBy(element, (annotation) ->
            annotation.annotationType().isAnnotationPresent(ValidateAs.class)
        );

        if (ObjectUtils.isEmpty(constraints)) {
            return null;
        }

        if (element instanceof Field) {
            return new FieldValidationPoint((Field) element, constraints);
        }

        throw new IllegalArgumentException( format("No implementation of ConstraintTarget found for %s", element) );
    }
}

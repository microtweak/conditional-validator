package com.github.microtweak.validator.conditional.core.internal.annotated;

import com.github.microtweak.validator.conditional.core.WhenActivatedValidateAs;
import com.github.microtweak.validator.conditional.core.internal.helper.AnnotationHelper;
import org.apache.commons.lang3.ObjectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

import static java.lang.String.format;

public interface ConstraintTarget {

    String getName();

    Class<?> getDeclaringClass();

    Type getType();

    List<Annotation> getConstraints();

    Object getTargetValue(Object bean);

    static ConstraintTarget of(AnnotatedElement element) {
        final List<Annotation> constraints = AnnotationHelper.findAnnotationsBy(element, (annotation) ->
            annotation.annotationType().isAnnotationPresent(WhenActivatedValidateAs.class)
        );

        if (ObjectUtils.isEmpty(constraints)) {
            return null;
        }

        if (element instanceof Field) {
            return new FieldConstraintTarget((Field) element, constraints);
        }

        throw new IllegalArgumentException( format("No implementation of ConstraintTarget found for %s", element) );
    }
}

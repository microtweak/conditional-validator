package com.github.microtweak.validator.conditional.internal;

import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;

@Getter
public class FakeAnnotatedElement implements AnnotatedElement {

    private final Annotation[] annotations;

    public FakeAnnotatedElement(Annotation... annotations) {
        this.annotations = annotations;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return (T) Arrays.stream(annotations)
            .filter(a -> a.annotationType().equals(annotationClass))
            .findFirst()
            .orElse(null);
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
        return getAnnotations();
    }

}

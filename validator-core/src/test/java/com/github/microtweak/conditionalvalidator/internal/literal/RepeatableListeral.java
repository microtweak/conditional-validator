package com.github.microtweak.conditionalvalidator.internal.literal;

import java.lang.annotation.Annotation;

public class RepeatableListeral<A extends Annotation> implements Annotation {

    private final A[] value;

    @SafeVarargs
    public RepeatableListeral(A... value) {
        this.value = value;
    }

    public A[] value() {
        return this.value;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return RepeatableListeral.class;
    }

}

package com.github.microtweak.validator.conditional.core.internal.helper;

import org.apache.commons.lang3.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

class FakeAnnotationInvocationHandler<A extends Annotation> implements InvocationHandler {

    private static final List<String> IGNORED_ATTRIBUTES = asList("equals", "hashCode", "toString", "annotationType");

    private final Class<A> annotationType;
    private final Map<String, Object> attributes;

    public FakeAnnotationInvocationHandler(Class<A> annotationType, Map<String, Object> attributes) {
        this.annotationType = annotationType;
        this.attributes = defaultIfNull(attributes, emptyMap());

        IGNORED_ATTRIBUTES.forEach(this.attributes::remove);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final String name = method.getName();
        final A annotationProxy = (A) proxy;

        switch (name) {
            case "equals":
                return AnnotationUtils.equals(annotationProxy, (Annotation) args[0]);

            case "hashCode":
                return AnnotationUtils.hashCode(annotationProxy);

            case "toString":
                return AnnotationUtils.toString(annotationProxy);

            case "annotationType":
                return annotationType;

            default:
                return attributes.get(name);
        }
    }

}

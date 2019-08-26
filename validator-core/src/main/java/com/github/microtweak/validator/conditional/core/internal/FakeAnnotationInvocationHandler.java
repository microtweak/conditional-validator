package com.github.microtweak.validator.conditional.core.internal;

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

    private Class<A> annotationType;
    private Map<String, Object> attributes;

    public FakeAnnotationInvocationHandler(Class<A> annotationType, Map<String, Object> attributes) {
        this.annotationType = annotationType;
        this.attributes = defaultIfNull(attributes, emptyMap());

        IGNORED_ATTRIBUTES.forEach(this.attributes::remove);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final String name = method.getName();

        switch (name) {
            case "equals":
                return AnnotationUtils.equals((Annotation) proxy, (Annotation) args[0]);

            case "hashCode":
                return AnnotationUtils.hashCode((A) proxy);

            case "toString":
                return AnnotationUtils.toString((A) proxy);

            case "annotationType":
                return annotationType;

            default:
                return attributes.get(name);
        }
    }

}

package com.github.microtweak.validator.conditional.core.internal.helper;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

@AllArgsConstructor
class FakeAnnotationInvocationHandler<A extends Annotation> implements InvocationHandler {

    private final Class<A> annotationType;
    private final Map<String, Object> attributes;

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
                return attributes.containsKey(name) ? attributes.get(name) : method.invoke(attributes);
        }
    }

}

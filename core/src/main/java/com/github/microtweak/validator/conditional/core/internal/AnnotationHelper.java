package com.github.microtweak.validator.conditional.core.internal;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.apache.commons.lang3.ArrayUtils.contains;
import static org.apache.commons.lang3.ArrayUtils.toArray;

public final class AnnotationHelper {

    public static Map<String, Object> readAllAtributeExcept(Annotation annotation, String... exceptAttributes) {
        final Map<String, Object> attributes = new HashMap<>();

        for (Method method : annotation.getClass().getDeclaredMethods()) {
            if (method.getParameterCount() > 0 || contains(exceptAttributes, method.getName())) {
                continue;
            }

            Object value = readAttribute(annotation, method);
            attributes.put(method.getName(), value);
        }

        return attributes;
    }

    public static Map<String, Object> readAllAttributes(Annotation annotation) {
        final Map<String, Object> attributes = new HashMap<>();

        for (Method method : annotation.getClass().getDeclaredMethods()) {
            Object value = readAttribute(annotation, method);
            attributes.put(method.getName(), value);
        }

        return attributes;
    }

    public static Object readAttribute(Annotation annotation, Method method) {
        try {
            return method.invoke(annotation);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    public static <R extends Serializable> R readAttribute(Annotation annotation, String attributeName, Class<R> returnType) throws NoSuchMethodException {
        Method method = annotation.annotationType().getDeclaredMethod(attributeName);
        Object v = readAttribute(annotation, method);
        return returnType.cast( v );
    }

    public static <A extends Annotation> A createAnnotation(Class<A> annotationType, Map<String, Object> attributes) {
        InvocationHandler h = new FakeAnnotationInvocationHandler(annotationType, attributes);
        return (A) Proxy.newProxyInstance(annotationType.getClassLoader(), toArray(annotationType), h);
    }

    public static <A extends Annotation> boolean hasAnyAnnotationWithAnnotation(AnnotatedElement element, Class<A> annotationType) {
        for (Annotation an : element.getAnnotations()) {
            if (an.annotationType().isAnnotationPresent(annotationType)) {
                return true;
            }
        }
        return false;
    }

    public static <A extends Annotation> Annotation[] getAnnotationsWithAnnotation(AnnotatedElement element, Class<A> annotationType) {
        return Stream.of( element.getAnnotations() )
                .filter(a -> a.annotationType().isAnnotationPresent(annotationType))
                .toArray(Annotation[]::new);
    }

}


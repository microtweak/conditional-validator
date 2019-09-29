package com.github.microtweak.validator.conditional.core.internal;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.apache.commons.lang3.ArrayUtils.*;

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

    public static Annotation[] unwrapRepeatableAnnotations(Annotation annotation) {
        try {
            Method attr = annotation.annotationType().getDeclaredMethod("value");

            return Optional.of( attr.getReturnType() )
                    .filter(r -> r.isArray())
                    .filter(r -> r.getComponentType().isAnnotationPresent(Repeatable.class))
                    .map(r -> (Annotation[]) readAttribute(annotation, attr))
                    .orElse( new Annotation[0] );
        } catch (NoSuchMethodException e) {
            return new Annotation[0];
        }
    }

    public static <A extends Annotation> Annotation[] getAnnotationsWithAnnotation(AnnotatedElement element, Class<A> annotationType) {
        return Stream.of( element.getAnnotations() )
                .flatMap(a -> {
                    Annotation[] repeatables = unwrapRepeatableAnnotations(a);
                    return isNotEmpty(repeatables) ? Stream.of(repeatables) : Stream.of(a);
                })
                .filter(a -> a.annotationType().isAnnotationPresent(annotationType))
                .toArray(Annotation[]::new);
    }

}


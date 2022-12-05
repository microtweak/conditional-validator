package com.github.microtweak.validator.conditional.core.internal.helper;

import org.apache.commons.lang3.reflect.MethodUtils;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
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

    @SuppressWarnings("unchecked")
    public static <A extends Annotation> A createAnnotation(Class<A> annotationType, Map<String, Object> attributes) {
        InvocationHandler h = new FakeAnnotationInvocationHandler<>(annotationType, attributes);
        return (A) Proxy.newProxyInstance(annotationType.getClassLoader(), toArray(annotationType), h);
    }

    private static Stream<Annotation> unwrapRepeatableAnnotations(Annotation annotation) {
        final Method valueAttr = MethodUtils.getAccessibleMethod(annotation.annotationType(), "value");

        return Optional.ofNullable(valueAttr)
            .map(Method::getReturnType)
            .filter(clazz ->
                clazz.isArray() && clazz.getComponentType().isAnnotationPresent(Repeatable.class)
            )
            .map(clazz -> (Annotation[]) readAttribute(annotation, valueAttr))
            .map(Stream::of)
            .orElseGet(() -> Stream.of(annotation));
    }

    public static List<Annotation> findAnnotationsBy(AnnotatedElement element, Predicate<Annotation> predicate) {
        return Stream.of( element.getAnnotations() )
            .flatMap(AnnotationHelper::unwrapRepeatableAnnotations)
            .filter(predicate)
            .collect(Collectors.toList());
    }

}


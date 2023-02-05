package com.github.microtweak.validator.conditional.core.internal.helper;

import org.apache.commons.lang3.reflect.MethodUtils;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.ArrayUtils.contains;
import static org.apache.commons.lang3.ArrayUtils.toArray;

public final class AnnotationHelper {

    public static Map<String, Object> readAllAttributes(Annotation annotation) {
        return readAllAttributesByCriteria(annotation, (method) -> true);
    }

    public static Map<String, Object> readAllAttributesExcept(Annotation annotation, String... exceptAttributes) {
        return readAllAttributesByCriteria(annotation, (method) -> !contains(exceptAttributes, method.getName()));
    }

    public static <R extends Serializable> R readAttribute(Annotation annotation, String attributeName, Class<R> returnType) throws NoSuchMethodException {
        final Method attribute = annotation.annotationType().getDeclaredMethod(attributeName);
        return readAttribute(annotation, attribute, returnType);
    }

    private static Map<String, Object> readAllAttributesByCriteria(Annotation annotation, Predicate<Method> predicate) {
        return Arrays.stream( annotation.annotationType().getDeclaredMethods() )
            .filter(attr -> attr.getParameterCount() == 0)
            .filter(predicate)
            .map(attr -> {
                final Object value = readAttribute(annotation, attr, Object.class);
                return value != null
                    ? new AbstractMap.SimpleEntry<>(attr.getName(), value)
                    : null;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static <R> R readAttribute(Annotation annotation, Method attribute, Class<R> returnType) {
        try {
            final Object value = attribute.invoke(annotation);
            return returnType.cast(value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <A extends Annotation> A createAnnotation(Class<A> annotationType, Map<String, Object> attributes) {
        final InvocationHandler h = new FakeAnnotationInvocationHandler<>(annotationType, attributes);
        return (A) Proxy.newProxyInstance(annotationType.getClassLoader(), toArray(annotationType), h);
    }

    private static Stream<Annotation> unwrapRepeatableAnnotations(Annotation annotation) {
        final Method valueAttr = MethodUtils.getAccessibleMethod(annotation.annotationType(), "value");

        return Optional.ofNullable(valueAttr)
            .map(Method::getReturnType)
            .filter(clazz ->
                clazz.isArray() && clazz.getComponentType().isAnnotationPresent(Repeatable.class)
            )
            .map(clazz -> (Annotation[]) readAttribute(annotation, valueAttr, Object.class))
            .map(Stream::of)
            .orElseGet(() -> Stream.of(annotation));
    }

    public static List<Annotation> findAnnotationsBy(AnnotatedElement element, Predicate<Annotation> predicate) {
        return Arrays.stream( element.getAnnotations() )
            .flatMap(AnnotationHelper::unwrapRepeatableAnnotations)
            .filter(predicate)
            .collect(Collectors.toList());
    }

}


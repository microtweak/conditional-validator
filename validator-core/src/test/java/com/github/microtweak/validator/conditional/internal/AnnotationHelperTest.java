package com.github.microtweak.validator.conditional.internal;

import com.github.microtweak.validator.conditional.core.internal.helper.AnnotationHelper;
import com.github.microtweak.validator.conditional.internal.literal.FakeAnnotatedElement;
import com.github.microtweak.validator.conditional.internal.literal.NotEmptyLiteral;
import com.github.microtweak.validator.conditional.internal.literal.NotNullLiteral;
import com.github.microtweak.validator.conditional.junit5.CoreTest;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@CoreTest
public class AnnotationHelperTest {

    @Test
    public void readSingle() {
        final NotEmpty fakeNotEmpty = new NotEmptyLiteral();

        assertAll(
            () -> {
                final String message = AnnotationHelper.readAttribute(fakeNotEmpty, "message", String.class);
                assertEquals(fakeNotEmpty.message(), message);
            },
            () -> assertThrows(NoSuchMethodException.class, () -> AnnotationHelper.readAttribute(fakeNotEmpty, "nonExistentAttribute", String.class))
        );
    }

    @Test
    public void readAll() {
        final NotNull fakeNotNull = new NotNullLiteral();

        final Map<String, Object> attrs = AnnotationHelper.readAllAttributes(fakeNotNull);

        assertAll(
            () -> assertEquals(fakeNotNull.annotationType(), attrs.get("annotationType")),
            () -> assertEquals(fakeNotNull.message(), attrs.get("message")),
            () -> {
                final Class<?>[] groups = (Class<?>[]) attrs.get("groups");
                assertArrayEquals(fakeNotNull.groups(), groups);
            },
            () -> {
                final Class<?>[] payload = (Class<?>[]) attrs.get("payload");
                assertArrayEquals(fakeNotNull.payload(), payload);
            }
        );
    }

    @Test
    public void readAllExcept() {
        final NotNull fakeNotNull = new NotNullLiteral();

        final Map<String, Object> attrs = AnnotationHelper.readAllAtributeExcept(fakeNotNull, "annotationType");

        assertAll(
            () -> assertNull(attrs.get("annotationType")),
            () -> assertEquals(fakeNotNull.message(), attrs.get("message")),
            () -> assertArrayEquals(fakeNotNull.groups(), (Object[]) attrs.get("groups")),
            () -> assertArrayEquals(fakeNotNull.payload(), (Object[]) attrs.get("payload"))
        );
    }

    @Test
    public void createAnnotationsAtRuntime() {
        final Map<String, Object> attrs = new HashMap<>();
        attrs.put("message", "must not be empty");
        attrs.put("groups", new Class[0]);
        attrs.put("payload", new Class[0]);

        final NotEmpty runtime = AnnotationHelper.createAnnotation(NotEmpty.class, attrs);

        assertAll(
            () -> assertEquals(NotEmpty.class, runtime.annotationType()),
            () -> assertEquals(attrs.get("message"), runtime.message()),
            () -> assertArrayEquals((Object[]) attrs.get("groups"), runtime.groups()),
            () -> assertArrayEquals((Object[]) attrs.get("payload"), runtime.payload())
        );
    }

    @Test
    public void findNonRepeatableAnnotations() {
        final AnnotatedElement element = new FakeAnnotatedElement(
            new NotNullLiteral(),
            new NotEmptyLiteral()
        );

        final List<Annotation> result = AnnotationHelper.findAnnotationsBy(
            element, (a) -> a.annotationType().equals(NotNull.class)
        );

        assertAll(
            () -> assertEquals(1, result.size()),
            () -> assertEquals(NotNull.class, result.get(0).annotationType())
        );
    }

    @Test
    public void findRepeatableAnnotations() {
        final AnnotatedElement element = new FakeAnnotatedElement(
            NotNullLiteral.asList(
                new NotNullLiteral()
            ),
            NotEmptyLiteral.asList(
                new NotEmptyLiteral(),
                new NotEmptyLiteral()
            )
        );

        final List<Annotation> result = AnnotationHelper.findAnnotationsBy(element, a -> a.annotationType().equals(NotEmpty.class));

        assertAll(
            () -> assertEquals(2, result.size()),
            () -> {
                final Class<? extends Annotation> firstResult = result.get(0).annotationType();
                final Class<? extends Annotation> secondResult = result.get(0).annotationType();

                assertEquals(NotEmpty.class, firstResult);
                assertEquals(NotEmpty.class, secondResult);
            }
        );
    }

}

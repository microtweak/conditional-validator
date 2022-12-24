package com.github.microtweak.validator.conditional.internal;

import com.github.microtweak.validator.conditional.core.internal.helper.AnnotationHelper;
import com.github.microtweak.validator.conditional.internal.literal.ConstraintLiteral;
import com.github.microtweak.validator.conditional.internal.literal.NotEmptyListLiteral;
import com.github.microtweak.validator.conditional.internal.literal.NotNullListLiteral;
import com.github.microtweak.validator.conditional.internal.literal.RepeatableListeral;
import com.github.microtweak.validator.conditional.junit5.CoreTest;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
        final NotEmpty fakeNotEmpty = new ConstraintLiteral<>(NotEmpty.class).build();

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
        final Email fakeEmail = new ConstraintLiteral<>(Email.class)
            .attribute("regexp", ".*")
            .attribute("flags", new Pattern.Flag[0])
            .build();

        final Map<String, Object> attrs = AnnotationHelper.readAllAttributes(fakeEmail);

        assertAll(
            () -> assertEquals(fakeEmail.regexp(), attrs.get("regexp")),
            () -> assertEquals(fakeEmail.flags(), attrs.get("flags")),
            () -> assertEquals(fakeEmail.message(), attrs.get("message")),
            () -> {
                final Class<?>[] groups = (Class<?>[]) attrs.get("groups");
                assertArrayEquals(fakeEmail.groups(), groups);
            },
            () -> {
                final Class<?>[] payload = (Class<?>[]) attrs.get("payload");
                assertArrayEquals(fakeEmail.payload(), payload);
            }
        );
    }

    @Test
    public void readAllExcept() {
        final Email fakeEmail = new ConstraintLiteral<>(Email.class)
            .attribute("regexp", ".*")
            .attribute("flags", new Pattern.Flag[0])
            .build();

        final Map<String, Object> attrs = AnnotationHelper.readAllAtributeExcept(fakeEmail, "regexp");

        assertAll(
            () -> assertNull(attrs.get("regexp")),
            () -> assertEquals(fakeEmail.message(), attrs.get("message")),
            () -> assertArrayEquals(fakeEmail.groups(), (Object[]) attrs.get("groups")),
            () -> assertArrayEquals(fakeEmail.payload(), (Object[]) attrs.get("payload"))
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
            new ConstraintLiteral<>(NotNull.class).build(),
            new ConstraintLiteral<>(NotEmpty.class).build()
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
            new NotNullListLiteral(
                new ConstraintLiteral<>(NotNull.class).build()
            ),
            new NotEmptyListLiteral(
                new ConstraintLiteral<>(NotEmpty.class).build(),
                new ConstraintLiteral<>(NotEmpty.class).build()
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

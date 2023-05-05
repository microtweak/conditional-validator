package com.github.microtweak.validator.conditional.tests;

import com.github.microtweak.validator.conditional.core.ValidateAs;
import com.github.microtweak.validator.conditional.tags.CheckListTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import javax.validation.Constraint;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@CheckListTest
public class ConditionalValidatorRequirementsTests {

    private static final String[] PACKAGES_TO_SCAN = {
        "com.github.microtweak.validator.conditional.core.constraint",
        "com.github.microtweak.validator.conditional.hv.constraint",
        "com.github.microtweak.validator.conditional.bval.constraint"
    };

    private static Stream<Class<? extends Annotation>> provideAllConditionalConstraintsSource() {
        final ClassLoader currentThreadClassloader = Thread.currentThread().getContextClassLoader();
        final ConfigurationBuilder configurationBuilder = new ConfigurationBuilder()
            .setInputsFilter(className -> StringUtils.containsNone(className, "$"));

        Stream.of(PACKAGES_TO_SCAN)
            .map(pkg -> currentThreadClassloader.getResource(pkg.replace(".", "/")) )
            .filter(Objects::nonNull)
            .forEach(configurationBuilder::addUrls);

        return new Reflections(configurationBuilder).getSubTypesOf(Annotation.class).stream();
    }

    @ParameterizedTest
    @MethodSource("provideAllConditionalConstraintsSource")
    public void beanValidationBasicAttributes(Class<Annotation> constraint) throws NoSuchMethodException {
        Method message = constraint.getDeclaredMethod("message");
        assertAll(
            () -> assertNotNull( message ),
            () -> assertEquals(String.class, message.getReturnType())
        );

        Method groups = constraint.getDeclaredMethod("groups");
        assertAll(
            () -> assertNotNull( groups ),
            () -> assertEquals(Class[].class, groups.getReturnType())
        );

        Method payload = constraint.getDeclaredMethod("payload");
        assertAll(
            () -> assertNotNull( payload ),
            () -> assertEquals(Class[].class, payload.getReturnType())
        );
    }

    @ParameterizedTest
    @MethodSource("provideAllConditionalConstraintsSource")
    public void isConditionalValidatorConstraint(Class<Annotation> constraint) throws NoSuchMethodException {
        assertTrue( constraint.getSimpleName().endsWith("When") );

        ValidateAs validateAs = constraint.getAnnotation(ValidateAs.class);

        assertAll(
            () -> assertNotNull(validateAs),
            () -> assertTrue( validateAs.value().isAnnotationPresent(Constraint.class) ),
            () -> assertTrue( constraint.getSimpleName().startsWith( validateAs.value().getSimpleName() ) )
        );

        Method expression = constraint.getDeclaredMethod("expression");
        assertAll(
            () -> assertNotNull( expression ),
            () -> assertEquals(String.class, expression.getReturnType())
        );
    }

}

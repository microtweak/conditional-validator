package com.github.microtweak.validator.conditional.core;

import com.github.microtweak.validator.conditional.junit5.BeanValidationConstraintSource;
import com.github.microtweak.validator.conditional.junit5.ProviderTest;
import org.junit.jupiter.params.ParameterizedTest;

import javax.validation.Constraint;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@ProviderTest
public class ConditionalValidatorRequirementsTests {

    @ParameterizedTest
    @BeanValidationConstraintSource({
        "com.github.microtweak.validator.conditional.core.constraint",
        "com.github.microtweak.validator.conditional.hv.constraint",
        "com.github.microtweak.validator.conditional.bval.constraint"
    })
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
    @BeanValidationConstraintSource({
            "com.github.microtweak.validator.conditional.core.constraint",
            "com.github.microtweak.validator.conditional.hv.constraint",
            "com.github.microtweak.validator.conditional.bval.constraint"
    })
    public void isConditionalValidatorConstraint(Class<Annotation> constraint) throws NoSuchMethodException {
        assertTrue( constraint.getSimpleName().endsWith("When") );

        WhenActivatedValidateAs validateAs = constraint.getAnnotation(WhenActivatedValidateAs.class);

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

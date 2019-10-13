package com.github.microtweak.validator.conditional.tests;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public abstract class BaseConstraintViolationTest {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public <T> Set<ConstraintViolation<T>> applyValidation(T object, Class<?>... groups) {
        return new HashSet<>( validator.validate(object, groups) );
    }

    protected <T> void assertHasPropertyViolation(Set<ConstraintViolation<T>> violations, String property) {
        assertNotNull( findViolationByProperty(violations, property) );
    }

    protected <T> void assertHasNotPropertyViolation(Set<ConstraintViolation<T>> violations, String property) {
        assertNull( findViolationByProperty(violations, property) );
    }

    private <T> ConstraintViolation<T> findViolationByProperty(Set<ConstraintViolation<T>> violations, String property) {
        return violations.stream()
                .filter(v -> v.getPropertyPath().toString().equals(property))
                .findFirst()
                .orElse( null );
    }

}

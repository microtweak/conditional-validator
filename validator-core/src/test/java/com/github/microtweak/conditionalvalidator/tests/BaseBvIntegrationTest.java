package com.github.microtweak.conditionalvalidator.tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public abstract class BaseBvIntegrationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void setUpAll() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void destroyAll() {
        validatorFactory.close();

        validator = null;
        validatorFactory = null;
    }

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

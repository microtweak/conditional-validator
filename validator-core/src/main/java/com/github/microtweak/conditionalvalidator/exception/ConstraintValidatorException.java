package com.github.microtweak.conditionalvalidator.exception;

public class ConstraintValidatorException extends RuntimeException {

    public ConstraintValidatorException(String message) {
        super(message);
    }

    public ConstraintValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

}

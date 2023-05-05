package com.github.microtweak.validator.conditional.core;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicates which Bean Validation constraint should be used when the conditional constraint is enabled
 */
@Target({ ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface ValidateAs {

    /**
     * BeanValidation annotation/constraint class to use
     */
    Class<? extends Annotation> value();

}
package com.github.microtweak.validator.conditional.junit5;

import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.*;

@ArgumentsSource(BeanValidationConstraintArgumentProvider.class)
@Documented
@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface BeanValidationConstraintSource {

    String[] value();

}

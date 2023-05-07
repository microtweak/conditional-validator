package com.github.microtweak.conditionalvalidator.constraint;

import com.github.microtweak.conditionalvalidator.ValidateAs;

import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Conditional version of constraint {@link NotNull @NotNull}
 */
@ValidateAs(NotNull.class)
@Repeatable(NotNullWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface NotNullWhen {

    String expression();

    String message() default "{jakarta.validation.constraints.NotNull.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    /**
     * Conditional version of constraint {@link NotNull.List @NotNull.List}
     */
    @Documented
    @Target({ METHOD, FIELD })
    @Retention(RUNTIME)
    @interface List {
        NotNullWhen[] value();
    }

}


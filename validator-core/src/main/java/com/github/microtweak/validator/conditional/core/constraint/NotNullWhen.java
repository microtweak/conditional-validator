package com.github.microtweak.validator.conditional.core.constraint;

import com.github.microtweak.validator.conditional.core.WhenActivatedValidateAs;

import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@WhenActivatedValidateAs(NotNull.class)
@Repeatable(NotNullWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface NotNullWhen {

    String expression();

    String message() default "{javax.validation.constraints.NotNull.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    @Documented
    @Target({ METHOD, FIELD })
    @Retention(RUNTIME)
    @interface List {
        NotNullWhen[] value();
    }

}


package com.github.microtweak.validator.conditional.core.constraint;

import com.github.microtweak.validator.conditional.core.ConditionalConstraint;

import javax.validation.Payload;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@ConditionalConstraint(NotEmpty.class)
@Repeatable(NotEmptyWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface NotEmptyWhen {

    String expression();

    String message() default "{javax.validation.constraints.NotEmpty.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    @Target({ METHOD, FIELD })
    @Retention(RUNTIME)
    @Documented
    @interface List {

        NotEmptyWhen[] value();

    }

}


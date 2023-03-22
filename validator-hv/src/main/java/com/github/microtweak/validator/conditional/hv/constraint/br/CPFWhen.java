package com.github.microtweak.validator.conditional.hv.constraint.br;

import com.github.microtweak.validator.conditional.core.WhenActivatedValidateAs;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Conditional version of constraint {@link CPF @CPF}
 */
@WhenActivatedValidateAs(CPF.class)
@Repeatable(CPFWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface CPFWhen {

    String expression();

    String message() default "{org.hibernate.validator.constraints.br.CPF.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    /**
     * Conditional version of constraint {@link CPF.List @CPF.List}
     */
    @Documented
    @Target({ METHOD, FIELD })
    @Retention(RUNTIME)
    @interface List {

        CPFWhen[] value();

    }

}


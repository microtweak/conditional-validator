package com.github.microtweak.conditionalvalidator.hv.constraint.br;

import com.github.microtweak.conditionalvalidator.ValidateAs;
import org.hibernate.validator.constraints.br.CNPJ;

import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Conditional version of constraint {@link CNPJ @CNPJ}
 */
@ValidateAs(CNPJ.class)
@Repeatable(CNPJWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface CNPJWhen {

    String expression();

    String message() default "{org.hibernate.validator.constraints.br.CNPJ.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    /**
     * Conditional version of constraint {@link CNPJ.List @CNPJ.List}
     */
    @Documented
    @Target({ METHOD, FIELD })
    @Retention(RUNTIME)
    @interface List {

        CNPJWhen[] value();

    }

}


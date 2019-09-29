package com.github.microtweak.validator.conditional.hv.constraint.br;

import com.github.microtweak.validator.conditional.core.WhenActivatedValidateAs;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@WhenActivatedValidateAs(CNPJ.class)
@Repeatable(CNPJWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface CNPJWhen {

    String expression();

    String message() default "{org.hibernate.validator.constraints.br.CNPJ.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    @Documented
    @Target({ METHOD, FIELD })
    @Retention(RUNTIME)
    @interface List {

        CNPJWhen[] value();

    }

}


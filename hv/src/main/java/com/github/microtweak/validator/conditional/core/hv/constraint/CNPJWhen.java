package com.github.microtweak.validator.conditional.core.hv.constraint;

import com.github.microtweak.validator.conditional.core.ConditionalConstraint;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@ConditionalConstraint(CNPJ.class)
@Repeatable(CNPJWhen.List.class)
@Documented
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface CNPJWhen {

    String expression();

    String message() default "{org.hibernate.validator.constraints.br.CNPJ.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    @interface List {

        CNPJWhen[] value();

    }

}


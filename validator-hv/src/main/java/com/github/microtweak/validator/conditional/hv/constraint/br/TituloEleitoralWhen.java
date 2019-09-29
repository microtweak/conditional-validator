package com.github.microtweak.validator.conditional.hv.constraint.br;

import com.github.microtweak.validator.conditional.core.WhenActivatedValidateAs;
import org.hibernate.validator.constraints.br.TituloEleitoral;

import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@WhenActivatedValidateAs(TituloEleitoral.class)
@Repeatable(TituloEleitoralWhen.List.class)
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface TituloEleitoralWhen {

	String expression();

	String message() default "{org.hibernate.validator.constraints.br.TituloEleitoral.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	@Documented
	@Target({ METHOD, FIELD })
	@Retention(RUNTIME)
	@interface List {
		TituloEleitoralWhen[] value();
	}
}

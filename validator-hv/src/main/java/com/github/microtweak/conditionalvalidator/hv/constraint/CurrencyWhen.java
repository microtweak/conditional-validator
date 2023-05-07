package com.github.microtweak.conditionalvalidator.hv.constraint;

import com.github.microtweak.conditionalvalidator.ValidateAs;
import org.hibernate.validator.constraints.Currency;

import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Conditional version of constraint {@link Currency @Currency}
 */
@ValidateAs(Currency.class)
@Repeatable(CurrencyWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface CurrencyWhen {

	String expression();

	String message() default "{org.hibernate.validator.constraints.Currency.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	String[] value();

	/**
	 * Conditional version of constraint {@link Currency.List @Currency.List}
	 */
	@Documented
	@Target({ METHOD, FIELD })
	@Retention(RUNTIME)
	@interface List {
		CurrencyWhen[] value();
	}
}

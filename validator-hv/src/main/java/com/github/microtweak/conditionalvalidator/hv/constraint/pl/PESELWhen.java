package com.github.microtweak.conditionalvalidator.hv.constraint.pl;

import com.github.microtweak.conditionalvalidator.ValidateAs;
import org.hibernate.validator.constraints.pl.PESEL;

import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Conditional version of constraint {@link PESEL @PESEL}
 */
@ValidateAs(PESEL.class)
@Repeatable(PESELWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface PESELWhen {

	String expression();

	String message() default "{org.hibernate.validator.constraints.pl.PESEL.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	/**
	 * Conditional version of constraint {@link PESEL.List @PESEL.List}
	 */
	@Documented
	@Target({ METHOD, FIELD })
	@Retention(RUNTIME)
	@interface List {
		PESELWhen[] value();
	}
}

package com.github.microtweak.conditionalvalidator.constraint;

import com.github.microtweak.conditionalvalidator.ValidateAs;

import jakarta.validation.Payload;
import jakarta.validation.constraints.NegativeOrZero;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Conditional version of constraint {@link NegativeOrZero @NegativeOrZero}
 */
@ValidateAs(NegativeOrZero.class)
@Repeatable(NegativeOrZeroWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface NegativeOrZeroWhen {

	String expression();

	String message() default "{jakarta.validation.constraints.NegativeOrZero.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	/**
	 * Conditional version of constraint {@link NegativeOrZero.List @NegativeOrZero.List}
	 */
	@Documented
	@Target({ METHOD, FIELD })
	@Retention(RUNTIME)
	@interface List {
		NegativeOrZeroWhen[] value();
	}
}

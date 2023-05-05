package com.github.microtweak.validator.conditional.core.constraint;

import com.github.microtweak.validator.conditional.core.ValidateAs;

import javax.validation.Payload;
import javax.validation.constraints.Digits;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Conditional version of constraint {@link Digits @Digits}
 */
@ValidateAs(Digits.class)
@Repeatable(DigitsWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface DigitsWhen {

	String expression();

	String message() default "{javax.validation.constraints.Digits.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	int integer();

	/**
	 * @return maximum number of fractional digits accepted for this number
	 */
	int fraction();

	/**
	 * Conditional version of constraint {@link Digits.List @Digits.List}
	 */
	@Documented
	@Target({ METHOD, FIELD })
	@Retention(RUNTIME)
	@interface List {
		DigitsWhen[] value();
	}
}

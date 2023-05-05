package com.github.microtweak.validator.conditional.core.constraint;

import com.github.microtweak.validator.conditional.core.ValidateAs;

import javax.validation.Payload;
import javax.validation.constraints.PositiveOrZero;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Conditional version of constraint {@link PositiveOrZero @PositiveOrZero}
 */
@ValidateAs(PositiveOrZero.class)
@Repeatable(PositiveOrZeroWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface PositiveOrZeroWhen {

	String expression();

	String message() default "{javax.validation.constraints.PositiveOrZero.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	/**
	 * Conditional version of constraint {@link PositiveOrZero.List @PositiveOrZero.List}
	 */
	@Documented
	@Target({ METHOD, FIELD })
	@Retention(RUNTIME)
	@interface List {
		PositiveOrZeroWhen[] value();
	}
}

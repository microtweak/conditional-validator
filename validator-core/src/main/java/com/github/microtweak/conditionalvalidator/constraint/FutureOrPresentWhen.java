package com.github.microtweak.conditionalvalidator.constraint;

import com.github.microtweak.conditionalvalidator.ValidateAs;

import javax.validation.Payload;
import javax.validation.constraints.FutureOrPresent;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Conditional version of constraint {@link FutureOrPresent @FutureOrPresent}
 */
@ValidateAs(FutureOrPresent.class)
@Repeatable(FutureOrPresentWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface FutureOrPresentWhen {

	String expression();

	String message() default "{javax.validation.constraints.FutureOrPresent.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	/**
	 * Conditional version of constraint {@link FutureOrPresent.List @FutureOrPresent.List}
	 */
	@Documented
	@Target({ METHOD, FIELD })
	@Retention(RUNTIME)
	@interface List {
		FutureOrPresentWhen[] value();
	}
}

package com.github.microtweak.conditionalvalidator.constraint;

import com.github.microtweak.conditionalvalidator.ValidateAs;

import jakarta.validation.Payload;
import jakarta.validation.constraints.PastOrPresent;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Conditional version of constraint {@link PastOrPresent @PastOrPresent}
 */
@ValidateAs(PastOrPresent.class)
@Repeatable(PastOrPresentWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface PastOrPresentWhen {

	String expression();

	String message() default "{jakarta.validation.constraints.PastOrPresent.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	/**
	 * Conditional version of constraint {@link PastOrPresent.List @PastOrPresent.List}
	 */
	@Documented
	@Target({ METHOD, FIELD })
	@Retention(RUNTIME)
	@interface List {
		PastOrPresentWhen[] value();
	}
}

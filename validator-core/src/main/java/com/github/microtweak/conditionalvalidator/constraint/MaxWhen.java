package com.github.microtweak.conditionalvalidator.constraint;

import com.github.microtweak.conditionalvalidator.ValidateAs;

import jakarta.validation.Payload;
import jakarta.validation.constraints.Max;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Conditional version of constraint {@link Max @Max}
 */
@ValidateAs(Max.class)
@Repeatable(MaxWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface MaxWhen {

	String expression();

	String message() default "{jakarta.validation.constraints.Max.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	/**
	 * @return value the element must be lower or equal to
	 */
	long value();

	/**
	 * Conditional version of constraint {@link Max.List @Max.List}
	 */
	@Documented
	@Target({ METHOD, FIELD })
	@Retention(RUNTIME)
	@interface List {
		MaxWhen[] value();
	}
}

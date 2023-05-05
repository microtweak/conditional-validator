package com.github.microtweak.validator.conditional.core.constraint;

import com.github.microtweak.validator.conditional.core.ValidateAs;

import javax.validation.Payload;
import javax.validation.constraints.DecimalMin;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Conditional version of constraint {@link DecimalMin @DecimalMin}
 */
@ValidateAs(DecimalMin.class)
@Repeatable(DecimalMinWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface DecimalMinWhen {

	String expression();

	String message() default "{javax.validation.constraints.DecimalMin.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	String value();

	boolean inclusive() default true;

	/**
	 * Conditional version of constraint {@link DecimalMin.List @DecimalMin.List}
	 */
	@Documented
	@Target({ METHOD, FIELD })
	@Retention(RUNTIME)
	@interface List {
		DecimalMinWhen[] value();
	}
}

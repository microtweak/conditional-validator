package com.github.microtweak.conditionalvalidator.hv.constraint.time;

import com.github.microtweak.conditionalvalidator.ValidateAs;
import org.hibernate.validator.constraints.time.DurationMax;

import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Conditional version of constraint {@link DurationMax @DurationMax}
 */
@ValidateAs(DurationMax.class)
@Repeatable(DurationMaxWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface DurationMaxWhen {

	String expression();

	String message() default "{org.hibernate.validator.constraints.time.DurationMax.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	long days() default 0;

	long hours() default 0;

	long minutes() default 0;

	long seconds() default 0;

	long millis() default 0;

	long nanos() default 0;

	/**
	 * Specifies whether the specified maximum is inclusive or exclusive. By default, it is inclusive.
	 *
	 * @return {@code true} if the value must be smaller or equal to the specified maximum,
	 *         {@code false} if the value must be smaller
	 *
	 */
	boolean inclusive() default true;

	/**
	 * Conditional version of constraint {@link DurationMax.List @DurationMax.List}
	 */
	@Documented
	@Target({ METHOD, FIELD })
	@Retention(RUNTIME)
	@interface List {
		DurationMaxWhen[] value();
	}
}

package com.github.microtweak.validator.conditional.hv.constraint.time;

import com.github.microtweak.validator.conditional.core.WhenActivatedValidateAs;
import org.hibernate.validator.constraints.time.DurationMin;

import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Conditional version of constraint {@link DurationMin @DurationMin}
 */
@WhenActivatedValidateAs(DurationMin.class)
@Repeatable(DurationMinWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface DurationMinWhen {

	String expression();

	String message() default "{org.hibernate.validator.constraints.time.DurationMin.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	long days() default 0;

	long hours() default 0;

	long minutes() default 0;

	long seconds() default 0;

	long millis() default 0;

	long nanos() default 0;

	/**
	 * Specifies whether the specified minimum is inclusive or exclusive. By default, it is inclusive.
	 *
	 * @return {@code true} if the value must be higher or equal to the specified minimum,
	 *         {@code false} if the value must be higher
	 *
	 */
	boolean inclusive() default true;

	/**
	 * Conditional version of constraint {@link DurationMin.List @DurationMin.List}
	 */
	@Documented
	@Target({ METHOD, FIELD })
	@Retention(RUNTIME)
	@interface List {

		DurationMinWhen[] value();

	}
}

package com.github.microtweak.conditionalvalidator.hv.constraint;

import com.github.microtweak.conditionalvalidator.ValidateAs;
import org.hibernate.validator.constraints.Range;

import jakarta.validation.OverridesAttribute;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Conditional version of constraint {@link Range @Range}
 */
@ValidateAs(Range.class)
@Repeatable(RangeWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface RangeWhen {

	String expression();

	String message() default "{org.hibernate.validator.constraints.Range.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	@OverridesAttribute(constraint = Min.class, name = "value") long min() default 0;

	@OverridesAttribute(constraint = Max.class, name = "value") long max() default Long.MAX_VALUE;

	/**
	 * Conditional version of constraint {@link Range.List @Range.List}
	 */
	@Documented
	@Target({ METHOD, FIELD })
	@Retention(RUNTIME)
	@interface List {
		RangeWhen[] value();
	}
}

package com.github.microtweak.validator.conditional.hv.constraint;

import com.github.microtweak.validator.conditional.core.WhenActivatedValidateAs;
import org.hibernate.validator.constraints.Range;

import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@WhenActivatedValidateAs(Range.class)
@Repeatable(RangeWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface RangeWhen {

	@OverridesAttribute(constraint = Min.class, name = "value") long min() default 0;

	@OverridesAttribute(constraint = Max.class, name = "value") long max() default Long.MAX_VALUE;

	String message() default "{org.hibernate.validator.constraints.Range.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	@Documented
	@Target({ METHOD, FIELD })
	@Retention(RUNTIME)
	@interface List {
		RangeWhen[] value();
	}
}

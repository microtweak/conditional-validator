package com.github.microtweak.validator.conditional.core.constraint;

import com.github.microtweak.validator.conditional.core.WhenActivatedValidateAs;

import javax.validation.Payload;
import javax.validation.constraints.DecimalMax;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@WhenActivatedValidateAs(DecimalMax.class)
@Repeatable(DecimalMaxWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface DecimalMaxWhen {

	String message() default "{javax.validation.constraints.DecimalMax.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	String value();

	boolean inclusive() default true;

	@Documented
	@Target({ METHOD, FIELD })
	@Retention(RUNTIME)
	@interface List {

		DecimalMaxWhen[] value();

	}
}

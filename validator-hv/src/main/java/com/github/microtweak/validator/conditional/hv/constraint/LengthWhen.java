package com.github.microtweak.validator.conditional.hv.constraint;

import com.github.microtweak.validator.conditional.core.WhenActivatedValidateAs;
import org.hibernate.validator.constraints.Length;

import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@WhenActivatedValidateAs(Length.class)
@Repeatable(LengthWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface LengthWhen {

	int min() default 0;

	int max() default Integer.MAX_VALUE;

	String message() default "{org.hibernate.validator.constraints.Length.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	@Documented
	@Target({ METHOD, FIELD })
	@Retention(RUNTIME)
	@interface List {
		LengthWhen[] value();
	}
}

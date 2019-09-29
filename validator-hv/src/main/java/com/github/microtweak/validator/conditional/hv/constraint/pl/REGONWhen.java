package com.github.microtweak.validator.conditional.hv.constraint.pl;

import com.github.microtweak.validator.conditional.core.WhenActivatedValidateAs;
import org.hibernate.validator.constraints.pl.REGON;

import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@WhenActivatedValidateAs(REGON.class)
@Repeatable(REGONWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface REGONWhen {

	String message() default "{org.hibernate.validator.constraints.pl.REGON.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	@Documented
	@Target({ METHOD, FIELD })
	@Retention(RUNTIME)
	@interface List {
		REGONWhen[] value();
	}
}

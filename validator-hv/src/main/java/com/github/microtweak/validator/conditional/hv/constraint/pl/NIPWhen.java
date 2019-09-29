package com.github.microtweak.validator.conditional.hv.constraint.pl;

import com.github.microtweak.validator.conditional.core.WhenActivatedValidateAs;
import org.hibernate.validator.constraints.pl.NIP;

import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@WhenActivatedValidateAs(NIP.class)
@Repeatable(NIPWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface NIPWhen {

	String message() default "{org.hibernate.validator.constraints.pl.NIP.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	@Documented
	@Target({ METHOD, FIELD })
	@Retention(RUNTIME)
	@interface List {
		NIPWhen[] value();
	}
}

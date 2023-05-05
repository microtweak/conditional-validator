package com.github.microtweak.validator.conditional.hv.constraint.pl;

import com.github.microtweak.validator.conditional.core.ValidateAs;
import org.hibernate.validator.constraints.pl.NIP;

import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Conditional version of constraint {@link NIP @NIP}
 */
@ValidateAs(NIP.class)
@Repeatable(NIPWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface NIPWhen {

	String expression();

	String message() default "{org.hibernate.validator.constraints.pl.NIP.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	/**
	 * Conditional version of constraint {@link NIP.List @NIP.List}
	 */
	@Documented
	@Target({ METHOD, FIELD })
	@Retention(RUNTIME)
	@interface List {
		NIPWhen[] value();
	}
}

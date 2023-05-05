package com.github.microtweak.validator.conditional.hv.constraint;

import com.github.microtweak.validator.conditional.core.ValidateAs;
import org.hibernate.validator.constraints.EAN;
import org.hibernate.validator.constraints.EAN.Type;

import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Conditional version of constraint {@link EAN @EAN}
 */
@ValidateAs(EAN.class)
@Repeatable(EANWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface EANWhen {

	String expression();

	String message() default "{org.hibernate.validator.constraints.EAN.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	Type type() default Type.EAN13;

	/**
	 * Conditional version of constraint {@link EAN.List @EAN.List}
	 */
	@Documented
	@Target({ METHOD, FIELD })
	@Retention(RUNTIME)
	@interface List {
		EANWhen[] value();
	}

}

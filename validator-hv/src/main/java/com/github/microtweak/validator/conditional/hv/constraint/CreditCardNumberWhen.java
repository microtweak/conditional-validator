package com.github.microtweak.validator.conditional.hv.constraint;

import com.github.microtweak.validator.conditional.core.WhenActivatedValidateAs;
import org.hibernate.validator.constraints.CreditCardNumber;

import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@WhenActivatedValidateAs(CreditCardNumber.class)
@Repeatable(CreditCardNumberWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface CreditCardNumberWhen {

	String message() default "{org.hibernate.validator.constraints.CreditCardNumber.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	/**
	 * @return Whether non-digit characters in the validated input should be ignored ({@code true}) or result in a
	 * validation error ({@code false}). Default is {@code false}
	 */
	@OverridesAttribute(constraint = LuhnCheckWhen.class, name = "ignoreNonDigitCharacters")
	boolean ignoreNonDigitCharacters() default false;

	@Documented
	@Target({ METHOD, FIELD })
	@Retention(RUNTIME)
	@interface List {
		CreditCardNumberWhen[] value();
	}
}

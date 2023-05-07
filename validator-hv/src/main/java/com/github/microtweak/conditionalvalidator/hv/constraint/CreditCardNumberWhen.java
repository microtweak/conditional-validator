package com.github.microtweak.conditionalvalidator.hv.constraint;

import com.github.microtweak.conditionalvalidator.ValidateAs;
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

/**
 * Conditional version of constraint {@link CreditCardNumber @CreditCardNumber}
 */
@ValidateAs(CreditCardNumber.class)
@Repeatable(CreditCardNumberWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface CreditCardNumberWhen {

	String expression();

	String message() default "{org.hibernate.validator.constraints.CreditCardNumber.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	/**
	 * @return Whether non-digit characters in the validated input should be ignored ({@code true}) or result in a
	 * validation error ({@code false}). Default is {@code false}
	 */
	@OverridesAttribute(constraint = LuhnCheckWhen.class, name = "ignoreNonDigitCharacters")
	boolean ignoreNonDigitCharacters() default false;

	/**
	 * Conditional version of constraint {@link CreditCardNumber.List @CreditCardNumber.List}
	 */
	@Documented
	@Target({ METHOD, FIELD })
	@Retention(RUNTIME)
	@interface List {
		CreditCardNumberWhen[] value();
	}
}

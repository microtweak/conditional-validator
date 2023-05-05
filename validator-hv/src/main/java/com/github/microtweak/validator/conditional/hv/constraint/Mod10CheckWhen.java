package com.github.microtweak.validator.conditional.hv.constraint;

import com.github.microtweak.validator.conditional.core.ValidateAs;
import org.hibernate.validator.constraints.Mod10Check;

import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Conditional version of constraint {@link Mod10Check @Mod10Check}
 */
@ValidateAs(Mod10Check.class)
@Repeatable(Mod10CheckWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface Mod10CheckWhen {

	String expression();

	String message() default "{org.hibernate.validator.constraints.Mod10Check.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	/**
	 * @return The multiplier to be used for odd digits when calculating the Mod10 checksum.
	 */
	int multiplier() default 3;

	/**
	 * @return The weight to be used for even digits when calculating the Mod10 checksum.
	 */
	int weight() default 1;

	/**
	 * @return the start index (inclusive) for calculating the checksum. If not specified 0 is assumed.
	 */
	int startIndex() default 0;

	/**
	 * @return the end index (inclusive) for calculating the checksum. If not specified the whole value is considered.
	 */
	int endIndex() default Integer.MAX_VALUE;

	/**
	 * @return The index of the check digit in the input. Per default it is assumed that the check digit is the last
	 * digit of the specified range. If set, the digit at the specified index is used. If set
	 * the following must hold true:<br>
	 * {@code checkDigitIndex > 0 && (checkDigitIndex < startIndex || checkDigitIndex >= endIndex}.
	 */
	int checkDigitIndex() default -1;

	/**
	 * @return Whether non-digit characters in the validated input should be ignored ({@code true}) or result in a
	 * validation error ({@code false}).
	 */
	boolean ignoreNonDigitCharacters() default true;

	/**
	 * Conditional version of constraint {@link Mod10Check.List @Mod10Check.List}
	 */
	@Documented
	@Target({ METHOD, FIELD })
	@Retention(RUNTIME)
	@interface List {
		Mod10CheckWhen[] value();
	}
}

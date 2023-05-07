package com.github.microtweak.conditionalvalidator.hv.constraint;

import com.github.microtweak.conditionalvalidator.ValidateAs;
import org.hibernate.validator.constraints.Mod11Check;
import org.hibernate.validator.constraints.Mod11Check.ProcessingDirection;

import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Conditional version of constraint {@link Mod11Check @Mod11Check}
 */
@ValidateAs(Mod11Check.class)
@Repeatable(Mod11CheckWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface Mod11CheckWhen {

	String expression();

	String message() default "{org.hibernate.validator.constraints.Mod11Check.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	/**
	 * @return The threshold for the Mod11 algorithm multiplier growth, if no value is specified the multiplier will grow indefinitely
	 */
	int threshold() default Integer.MAX_VALUE;

	/**
	 * @return the start index (inclusive) for calculating the checksum. If not specified 0 is assumed.
	 */
	int startIndex() default 0;

	/**
	 * @return the end index (inclusive) for calculating the checksum. If not specified the whole value is considered
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
	boolean ignoreNonDigitCharacters() default false;

	/**
	 * @return The {@code char} that represents the check digit when the Mod11
	 * checksum equals 10. If not specified {@code 'X'} is assumed.
	 */
	char treatCheck10As() default 'X';

	/**
	 * @return The {@code char} that represents the check digit when the Mod11
	 * checksum equals 11. If not specified {@code '0'} is assumed.
	 */
	char treatCheck11As() default '0';

	/**
	 * @return Returns {@code RIGHT_TO_LEFT} if the Mod11 checksum must be done from the rightmost to the leftmost digit.
	 * e.g. Code 12345-?:
	 * <ul>
	 * <li>{@code RIGHT_TO_LEFT} the sum (5*2 + 4*3 + 3*4 + 2*5 + 1*6) with check digit 5</li>
	 * <li>{@code LEFT_TO_RIGHT} the sum (1*2 + 2*3 + 3*4 + 4*5 + 5*6) with check digit 7</li>
	 * </ul>
	 * If not specified {@code RIGHT_TO_LEFT} is assumed, it is the default Mod11 behavior.
	 */
	ProcessingDirection processingDirection() default ProcessingDirection.RIGHT_TO_LEFT;

	/**
	 * Conditional version of constraint {@link Mod11Check.List @Mod11Check.List}
	 */
	@Documented
	@Target({ METHOD, FIELD })
	@Retention(RUNTIME)
	@interface List {
		Mod11CheckWhen[] value();
	}

}

package com.github.microtweak.validator.conditional.core.constraint;

import com.github.microtweak.validator.conditional.core.WhenActivatedValidateAs;

import javax.validation.Payload;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern.Flag;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@WhenActivatedValidateAs(Email.class)
@Repeatable(EmailWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface EmailWhen {

	String expression();

	String message() default "{javax.validation.constraints.Email.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	/**
	 * @return an additional regular expression the annotated element must match. The default
	 * is any string ('.*')
	 */
	String regexp() default ".*";

	/**
	 * @return used in combination with {@link #regexp()} in order to specify a regular
	 * expression option
	 */
	Flag[] flags() default { };

	@Documented
	@Target({ METHOD, FIELD })
	@Retention(RUNTIME)
	@interface List {
		EmailWhen[] value();
	}
}

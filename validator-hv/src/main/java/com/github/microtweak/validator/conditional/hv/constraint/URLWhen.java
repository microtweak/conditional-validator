package com.github.microtweak.validator.conditional.hv.constraint;

import com.github.microtweak.validator.conditional.core.ValidateAs;
import org.hibernate.validator.constraints.URL;

import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Conditional version of constraint {@link URL @URL}
 */
@ValidateAs(URL.class)
@Repeatable(URLWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface URLWhen {

	String expression();

	String message() default "{org.hibernate.validator.constraints.URL.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	/**
	 * @return the protocol (scheme) the annotated string must match, e.g. ftp or http.
	 *         Per default any protocol is allowed
	 */
	String protocol() default "";

	/**
	 * @return the host the annotated string must match, e.g. localhost. Per default any host is allowed
	 */
	String host() default "";

	/**
	 * @return the port the annotated string must match, e.g. 80. Per default any port is allowed
	 */
	int port() default -1;

	/**
	 * @return an additional regular expression the annotated URL must match. The default is any string ('.*')
	 */
	@OverridesAttribute(constraint = Pattern.class, name = "regexp") String regexp() default ".*";

	/**
	 * @return used in combination with {@link #regexp()} in order to specify a regular expression option
	 */
	@OverridesAttribute(constraint = Pattern.class, name = "flags") Pattern.Flag[] flags() default { };

	/**
	 * Conditional version of constraint {@link URL.List @URL.List}
	 */
	@Documented
	@Target({ METHOD, FIELD })
	@Retention(RUNTIME)
	@interface List {
		URLWhen[] value();
	}
}

package com.github.microtweak.validator.conditional.hv.constraint;

import com.github.microtweak.validator.conditional.core.WhenActivatedValidateAs;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.Tag;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@WhenActivatedValidateAs(SafeHtml.class)
@Repeatable(SafeHtmlWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface SafeHtmlWhen {

	String expression();

	String message() default "{org.hibernate.validator.constraints.SafeHtml.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	/**
	 * @return The built-in whitelist type which will be applied to the rich text value
	 */
	WhiteListType whitelistType() default WhiteListType.RELAXED;

	/**
	 * @return Additional whitelist tags which are allowed on top of the tags specified by the
	 * {@link #whitelistType()}.
	 */
	String[] additionalTags() default { };

	/**
	 * @return Allows to specify additional whitelist tags with optional attributes and protocols.
	 */
	Tag[] additionalTagsWithAttributes() default { };

	/**
	 * @return Base URI used to resolve relative URIs to absolute ones. If not set, validation
	 * of HTML containing relative URIs will fail.
	 *
	 * @since 6.0
	 */
	String baseURI() default "";

	@Documented
	@Target({ METHOD, FIELD })
	@Retention(RUNTIME)
	@interface List {
		SafeHtmlWhen[] value();
	}

}

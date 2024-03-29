package com.github.microtweak.conditionalvalidator.hv.constraint;

import com.github.microtweak.conditionalvalidator.ValidateAs;
import org.hibernate.validator.constraints.CodePointLength;
import org.hibernate.validator.constraints.CodePointLength.NormalizationStrategy;

import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Conditional version of constraint {@link CodePointLength @CodePointLength}
 */
@ValidateAs(CodePointLength.class)
@Repeatable(CodePointLengthWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface CodePointLengthWhen {

	String expression();

	String message() default "{org.hibernate.validator.constraints.CodePointLength.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	int min() default 0;

	int max() default Integer.MAX_VALUE;

	NormalizationStrategy normalizationStrategy() default NormalizationStrategy.NONE;

	/**
	 * Conditional version of constraint {@link CodePointLength.List @CodePointLength.List}
	 */
	@Documented
	@Target({ METHOD, FIELD })
	@Retention(RUNTIME)
	@interface List {
		CodePointLengthWhen[] value();
	}

}


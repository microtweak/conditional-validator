package com.github.microtweak.validator.conditional.hv.constraint;

import com.github.microtweak.validator.conditional.core.WhenActivatedValidateAs;
import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.ISBN.Type;

import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@WhenActivatedValidateAs(ISBN.class)
@Repeatable(ISBNWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface ISBNWhen {

	String expression();

	String message() default "{org.hibernate.validator.constraints.ISBN.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	Type type() default Type.ISBN_13;

	@Documented
	@Target({ METHOD, FIELD })
	@Retention(RUNTIME)
	@interface List {
		ISBNWhen[] value();
	}

}

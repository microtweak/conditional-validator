package com.github.microtweak.validator.conditional.bval.constraint.net;

import com.github.microtweak.validator.conditional.core.WhenActivatedValidateAs;
import org.apache.bval.extras.constraints.net.InetAddress;

import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@WhenActivatedValidateAs(InetAddress.class)
@Documented
@Target({ FIELD, ANNOTATION_TYPE, PARAMETER })
@Retention(RUNTIME)
public @interface InetAddressWhen {

    String expression();

    String message() default "{org.apache.bval.extras.constraints.net.InetAddress.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

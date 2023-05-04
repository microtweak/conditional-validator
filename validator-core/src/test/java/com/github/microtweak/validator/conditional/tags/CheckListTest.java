package com.github.microtweak.validator.conditional.tags;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Tag("checklist")
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface CheckListTest {
}

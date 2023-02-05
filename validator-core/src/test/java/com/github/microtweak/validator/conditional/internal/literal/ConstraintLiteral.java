package com.github.microtweak.validator.conditional.internal.literal;

import com.github.microtweak.validator.conditional.core.WhenActivatedValidateAs;
import com.github.microtweak.validator.conditional.core.internal.helper.AnnotationHelper;
import lombok.RequiredArgsConstructor;

import javax.validation.Payload;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ConstraintLiteral<A extends Annotation> {

    private final Class<? extends A> annotationType;
    private final Map<String, Object> attributes = new HashMap<>();

    public ConstraintLiteral<A> attribute(String name, Object value) {
        attributes.put(name, value);
        return this;
    }

    public ConstraintLiteral<A> message(String message) {
        return attribute("message", message);
    }

    public ConstraintLiteral<A> groups(Class<?>[] groups) {
        return attribute("groups", groups);
    }

    public ConstraintLiteral<A> payload(Class<? extends Payload>[] payload) {
        return attribute("payload", payload);
    }

    public A build() {
        final Map<String, Object> attrs = new HashMap<>(this.attributes);
        attrs.putIfAbsent("message", "It is not valid");
        attrs.putIfAbsent("groups", new Class[0]);
        attrs.putIfAbsent("payload", new Class[0]);

        final boolean isCvConstraint = annotationType.isAnnotationPresent(WhenActivatedValidateAs.class);

        if (isCvConstraint && !attributes.containsKey("expression")) {
            attrs.putIfAbsent("expression", "true");
        }

        return AnnotationHelper.createAnnotation(annotationType, attrs);
    }

}

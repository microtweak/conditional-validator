package com.github.microtweak.conditionalvalidator.internal.literal;

import com.github.microtweak.conditionalvalidator.ValidateAs;
import com.github.microtweak.conditionalvalidator.internal.helper.AnnotationHelper;

import javax.validation.Payload;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class ConstraintLiteral<A extends Annotation> {

    private final Class<? extends A> annotationType;
    private final Class<? extends Annotation> actualAnnotationType;
    private final Map<String, Object> attributes = new HashMap<>();;

    public ConstraintLiteral(Class<? extends A> annotationType) {
        this.annotationType = annotationType;

        final ValidateAs validateAs = annotationType.getAnnotation(ValidateAs.class);
        this.actualAnnotationType = validateAs != null ? validateAs.value() : null;
    }

    private boolean isCvConstraint() {
        return actualAnnotationType != null;
    }

    private String getDefaultMessage() {
        final Class<? extends Annotation> annotationType = actualAnnotationType != null ? actualAnnotationType : this.annotationType;
        return "{" + annotationType.getName() + ".message}";
    }


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
        // attrs.putIfAbsent("message", "It is not valid");
        attrs.putIfAbsent("message", getDefaultMessage());
        attrs.putIfAbsent("groups", new Class[0]);
        attrs.putIfAbsent("payload", new Class[0]);

        if (isCvConstraint() && !attributes.containsKey("expression")) {
            attrs.putIfAbsent("expression", "true");
        }

        return AnnotationHelper.createAnnotation(annotationType, attrs);
    }

}

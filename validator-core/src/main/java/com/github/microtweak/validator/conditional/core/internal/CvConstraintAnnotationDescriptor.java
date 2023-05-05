package com.github.microtweak.validator.conditional.core.internal;

import com.github.microtweak.validator.conditional.core.ValidateAs;
import com.github.microtweak.validator.conditional.core.internal.helper.AnnotationHelper;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.Validate;

import javax.validation.ConstraintTarget;
import javax.validation.Payload;
import javax.validation.groups.Default;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

@ToString
@Getter
public class CvConstraintAnnotationDescriptor {

    private final Map<String, Object> attributes;
    private final String expression;
    private final String message;
    private final Set<Class<?>> groups;
    private final Set<Class<? extends Payload>> payload;
    private final ConstraintTarget validationAppliesTo;
    private final Annotation conditionalConstraint;
    private final Annotation actualBeanValidationConstraint;

    @SuppressWarnings("unchecked")
    public CvConstraintAnnotationDescriptor(Annotation conditionalConstraint) {
        final Class<? extends Annotation> actualConstraintType = conditionalConstraint.annotationType().getAnnotation(ValidateAs.class).value();

        if (actualConstraintType == null) {
            throw new IllegalArgumentException(
                format("Conditional constraint is not annotated with %s!", ValidateAs.class)
            );
        }

        this.attributes = AnnotationHelper.readAllAttributes(conditionalConstraint);

        this.expression = (String) attributes.remove("expression");
        Validate.notEmpty(expression, "The \"%s\" attribute of the \"%s\" constraint cannot be blank!", "expression", conditionalConstraint.annotationType());

        this.message = (String) attributes.get("message");
        Validate.notEmpty(message, "The Bean Validation constraint %s does not have the attribute \"%s\"!", conditionalConstraint.annotationType(), "message");

        this.groups = toImmutableSet((Class<?>[]) attributes.get("groups"), Default.class);
        this.payload = toImmutableSet((Class<? extends Payload>[]) attributes.get("payload"));

        this.validationAppliesTo = (ConstraintTarget) attributes.get("validationAppliesTo");

        this.conditionalConstraint = conditionalConstraint;
        this.actualBeanValidationConstraint = AnnotationHelper.createAnnotation(actualConstraintType, attributes);
    }

    @SafeVarargs
    private static <V> Set<V> toImmutableSet(V[] values, V... nullIf) {
        return Arrays
            .stream(isNotEmpty(values) ? values : nullIf)
            .collect( collectingAndThen(toSet(),Collections::unmodifiableSet) );
    }

}

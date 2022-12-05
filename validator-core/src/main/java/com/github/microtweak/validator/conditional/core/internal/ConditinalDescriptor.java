package com.github.microtweak.validator.conditional.core.internal;

import com.github.microtweak.validator.conditional.core.internal.annotated.ConstraintTarget;
import com.github.microtweak.validator.conditional.core.internal.helper.BeanValidationHelper;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.Validate;

import javax.validation.ConstraintValidator;
import java.lang.annotation.Annotation;

import static com.github.microtweak.validator.conditional.core.internal.helper.AnnotationHelper.readAttribute;

@Getter
@ToString
@EqualsAndHashCode(of = { "constraintTarget", "actualConstraint" })
public class ConditinalDescriptor {

    private final ConstraintTarget constraintTarget;
    private final Annotation actualConstraint;
    private final String constraintMessage;
    private final String expression;
    private final ConstraintValidator validator;

    public ConditinalDescriptor(ConstraintTarget constraintTarget, Annotation conditionalConstraint) {
        this.constraintTarget = constraintTarget;
        this.actualConstraint = BeanValidationHelper.getActualBeanValidationContraintOf(conditionalConstraint);

        try {
            this.constraintMessage = readAttribute(actualConstraint, "message", String.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("The Bean Validation constraint " + actualConstraint.annotationType() + " does not have the attribute \"message\"!");
        }

        try {
            this.expression = readAttribute(conditionalConstraint, "expression", String.class);
            Validate.notEmpty(expression);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("The conditional constraint " + conditionalConstraint.annotationType() + " does not have the attribute \"expression\"!");
        }

        this.validator = BeanValidationHelper.getConstraintValidatorOf(actualConstraint, (Class<?>) constraintTarget.getType());
    }

    public String getName() {
        return constraintTarget.getName();
    }

}

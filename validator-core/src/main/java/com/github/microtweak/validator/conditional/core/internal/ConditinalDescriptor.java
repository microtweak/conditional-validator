package com.github.microtweak.validator.conditional.core.internal;

import com.github.microtweak.validator.conditional.core.internal.annotated.ValidationPoint;
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
@EqualsAndHashCode(of = { "validationPoint", "actualConstraint" })
public class ConditinalDescriptor {

    private final ValidationPoint validationPoint;
    private final Annotation actualConstraint;
    private final String constraintMessage;
    private final String expression;
    private final Class<? extends ConstraintValidator> validatorClass;

    public ConditinalDescriptor(ValidationPoint validationPoint, Annotation conditionalConstraint) {
        this.validationPoint = validationPoint;
        this.actualConstraint = BeanValidationHelper.getActualBeanValidationContraintOf(conditionalConstraint);

        try {
            this.constraintMessage = readAttribute(actualConstraint, "message", String.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                "The Bean Validation constraint " + actualConstraint.annotationType() + " does not have the attribute \"message\"!"
            );
        }

        try {
            this.expression = readAttribute(conditionalConstraint, "expression", String.class);
            Validate.notEmpty(expression);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                "The conditional constraint " + conditionalConstraint.annotationType() + " does not have the attribute \"expression\"!"
            );
        }

        this.validatorClass = BeanValidationHelper.findConstraintValidatorClass(actualConstraint.annotationType(), validationPoint.getType());
    }

    public String getName() {
        return validationPoint.getName();
    }

}

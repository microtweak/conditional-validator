package com.github.microtweak.validator.conditional.core;

import com.github.microtweak.validator.conditional.core.internal.CvConstraintDescriptorImpl;
import com.github.microtweak.validator.conditional.core.internal.annotated.ValidationPoint;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

import static com.github.microtweak.validator.conditional.core.internal.helper.BeanValidationHelper.*;

@Slf4j
public class DelegatedConditionalConstraintValidator implements ConstraintValidator<ConditionalValidate, Object> {

    private ExpressionEvaluator evaluator;

    @Override
    public void initialize(ConditionalValidate conditionalConstraint) {
        log.trace("Initializing {} for {}", getClass().getSimpleName(), conditionalConstraint.annotationType());
        evaluator = new ExpressionEvaluator( conditionalConstraint.contextClasses() );
    }

    @Override
    public boolean isValid(Object validatedBean, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        boolean isAllConstraintsValid = true;

        for (final ValidationPoint validationPoint : getAllValidationPointsAt(validatedBean.getClass())) {
            for (final CvConstraintDescriptorImpl<Annotation> descriptor : getAllConstraintDescriptorOf(validationPoint)) {
                if (!evaluator.isTrueExpression(validatedBean, descriptor.getExpression())) {
                    continue;
                }

                final ConstraintValidator<Annotation, Object> validator = getInitializedConstraintValidator(descriptor);
                final Object value = validationPoint.getValidatedValue(validatedBean);

                if (!validator.isValid(value, context)) {
                    isAllConstraintsValid = false;

                    context.buildConstraintViolationWithTemplate( descriptor.getMessageTemplate() )
                        .addPropertyNode(validationPoint.getName())
                        .addConstraintViolation();
                }
            }
        }

        return isAllConstraintsValid;
    }

}

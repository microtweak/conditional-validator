package com.github.microtweak.validator.conditional.core;

import com.github.microtweak.validator.conditional.core.internal.ConditinalDescriptor;
import com.github.microtweak.validator.conditional.core.internal.helper.BeanValidationHelper;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

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
        final Set<ConditinalDescriptor> conditionalDescriptors = BeanValidationHelper.getConditinalDescriptorOf( validatedBean.getClass() );

        context.disableDefaultConstraintViolation();

        boolean isValid = true;

        for (ConditinalDescriptor descriptor : conditionalDescriptors) {
            if (!evaluator.isTrueExpression(validatedBean, descriptor.getExpression())) {
                continue;
            }

            final boolean isDelegatedValid = BeanValidationHelper.invokeConstraintValidator(validatedBean, descriptor, context);

            if (!isDelegatedValid) {
                isValid = false;

                context.buildConstraintViolationWithTemplate( descriptor.getConstraintMessage() )
                    .addPropertyNode( descriptor.getName() )
                    .addConstraintViolation();
            }
        }

        return isValid;
    }

}

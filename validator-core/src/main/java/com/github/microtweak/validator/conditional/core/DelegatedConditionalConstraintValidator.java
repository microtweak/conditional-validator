package com.github.microtweak.validator.conditional.core;

import com.github.microtweak.validator.conditional.core.internal.CvConstraintDescriptorImpl;
import com.github.microtweak.validator.conditional.core.internal.CvMessageInterpolatorContext;
import com.github.microtweak.validator.conditional.core.internal.annotated.ValidationPoint;
import com.github.microtweak.validator.conditional.core.internal.helper.BeanValidationHelper;
import com.github.microtweak.validator.conditional.core.internal.helper.ConstraintValidatorRegistrar;
import com.github.microtweak.validator.conditional.core.spi.BeanValidationImplementationProvider;
import com.github.microtweak.validator.conditional.core.spi.PlatformProvider;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.MessageInterpolator;
import javax.validation.metadata.ConstraintDescriptor;
import java.lang.annotation.Annotation;

@Slf4j
public class DelegatedConditionalConstraintValidator implements ConstraintValidator<ConditionalValidate, Object> {

    private static final PlatformProvider platform = PlatformProvider.getInstance();
    private static final BeanValidationImplementationProvider bvProvider = BeanValidationImplementationProvider.getInstance();
    private static final ConstraintValidatorRegistrar registrar = new ConstraintValidatorRegistrar();

    private ExpressionEvaluator evaluator;

    static {
        registrar.addValidators(bvProvider.getAvailableConstraintValidators());
    }

    @Override
    public void initialize(ConditionalValidate conditionalConstraint) {
        log.trace("Initializing {} for {}", getClass().getSimpleName(), conditionalConstraint.annotationType());
        evaluator = new ExpressionEvaluator( conditionalConstraint.contextClasses() );
    }

    @Override
    public boolean isValid(Object validatedBean, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        boolean isAllConstraintsValid = true;

        for (final ValidationPoint validationPoint : BeanValidationHelper.getAllValidationPointsAt(validatedBean.getClass())) {
            final Object value = validationPoint.getValidatedValue(validatedBean);

            for (final CvConstraintDescriptorImpl<Annotation> descriptor : BeanValidationHelper.getAllConstraintDescriptorOf(validationPoint, registrar)) {
                if (!evaluator.isTrueExpression(validatedBean, descriptor.getExpression())) {
                    continue;
                }

                final ConstraintValidator<Annotation, Object> validator = BeanValidationHelper.getInitializedConstraintValidator(platform.getConstraintValidatorFactory(), descriptor);

                if (!validator.isValid(value, context)) {
                    isAllConstraintsValid = false;

                    final String interpolatedMessage = getInterpolatedMessage(descriptor, value);

                    context.buildConstraintViolationWithTemplate(interpolatedMessage)
                        .addPropertyNode(validationPoint.getName())
                        .addConstraintViolation();
                }
            }
        }

        return isAllConstraintsValid;
    }

    private String getInterpolatedMessage(ConstraintDescriptor<?> constraintDescriptor, Object validatedValue) {
        final MessageInterpolator.Context msgInterpolatorCtx = new CvMessageInterpolatorContext(constraintDescriptor, validatedValue);
        return platform.getMessageInterpolator().interpolate(constraintDescriptor.getMessageTemplate(), msgInterpolatorCtx);
    }

}

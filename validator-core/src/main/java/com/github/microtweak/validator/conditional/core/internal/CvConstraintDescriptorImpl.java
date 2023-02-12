package com.github.microtweak.validator.conditional.core.internal;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.validation.*;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.ValidateUnwrappedValue;
import javax.validation.valueextraction.Unwrapping;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;

@Getter
@Slf4j
public class CvConstraintDescriptorImpl<T extends Annotation> implements ConstraintDescriptor<T> {

    private final T annotation;
    private final String expression;
    private final String messageTemplate;
    private final Set<Class<?>> groups;
    private final Set<Class<? extends Payload>> payload;
    private final ConstraintTarget validationAppliesTo;
    private final List<Class<? extends ConstraintValidator<T, ?>>> constraintValidatorClasses;
    private final Map<String, Object> attributes;
    private final Set<ConstraintDescriptor<?>> composingConstraints = Collections.emptySet();
    private final boolean reportAsSingleViolation;

    @SuppressWarnings("unchecked")
    public CvConstraintDescriptorImpl(CvConstraintAnnotationDescriptor annotationDescriptor, Class<? extends ConstraintValidator<T, ?>> constraintValidatorClass) {
        this.annotation = (T) annotationDescriptor.getActualBeanValidationConstraint();
        this.expression = annotationDescriptor.getExpression();
        this.messageTemplate = annotationDescriptor.getMessage();
        this.groups = annotationDescriptor.getGroups();
        this.payload = annotationDescriptor.getPayload();
        this.attributes = annotationDescriptor.getAttributes();
        this.validationAppliesTo = annotationDescriptor.getValidationAppliesTo();

        this.constraintValidatorClasses = Collections.singletonList(constraintValidatorClass);
        this.reportAsSingleViolation = annotation.annotationType().isAnnotationPresent(ReportAsSingleViolation.class);
    }

    @Override
    public ValidateUnwrappedValue getValueUnwrapping() {
        final Set<Class<? extends Payload>> payload = getPayload();
        final boolean isSkip = payload.contains( Unwrapping.Skip.class );

        if (payload.contains( Unwrapping.Unwrap.class )) {
            if (isSkip) {
                final String msg = "Invalid unwrapping configuration for constraint %s. You can only define one of 'Unwrapping.Skip' or 'Unwrapping.Unwrap'.";
                throw new ConstraintDeclarationException( format(msg, annotation.annotationType()) );
            }
            return ValidateUnwrappedValue.UNWRAP;
        }
        return isSkip ? ValidateUnwrappedValue.SKIP : ValidateUnwrappedValue.DEFAULT;
    }

    @Override
    public <U> U unwrap(Class<U> aClass) {
        throw new ValidationException("Unwrapping of ConstraintDescriptor is not supported.");
    }
}

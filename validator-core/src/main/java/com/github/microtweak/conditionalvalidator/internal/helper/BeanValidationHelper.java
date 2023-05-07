package com.github.microtweak.conditionalvalidator.internal.helper;

import com.github.microtweak.conditionalvalidator.exception.ConstraintValidatorException;
import com.github.microtweak.conditionalvalidator.internal.CvConstraintAnnotationDescriptor;
import com.github.microtweak.conditionalvalidator.internal.CvConstraintDescriptorImpl;
import com.github.microtweak.conditionalvalidator.internal.annotated.ValidationPoint;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorFactory;
import jakarta.validation.metadata.ConstraintDescriptor;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
public final class BeanValidationHelper {

    public static Set<ValidationPoint> getAllValidationPointsAt(Class<?> conditionalValidatedClass) {
        return FieldUtils.getAllFieldsList(conditionalValidatedClass).stream()
            .map(ValidationPoint::of)
            .filter(Objects::nonNull)
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @SuppressWarnings("unchecked")
    public static <A extends Annotation> Set<CvConstraintDescriptorImpl<A>> getAllConstraintDescriptorOf(ValidationPoint validationPoint, ConstraintValidatorRegistrar registrar) {
        return validationPoint.getConstraints().stream()
            .map(CvConstraintAnnotationDescriptor::new)
            .map(annotationDescriptor -> {
                final Class<A> actualBvConstraintType = (Class<A>) annotationDescriptor.getActualBeanValidationConstraint().annotationType();
                final Class<? extends ConstraintValidator<A, ?>> validatorClass = findConstraintValidatorClass(registrar, actualBvConstraintType, validationPoint.getType());

                return new CvConstraintDescriptorImpl<>(annotationDescriptor, validatorClass);
            })
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @SuppressWarnings("unchecked")
    public static <A extends Annotation> Class<? extends ConstraintValidator<A, ?>> findConstraintValidatorClass(ConstraintValidatorRegistrar registrar, Class<A> constraintClass, Class<?> validatedType) {
        Set<ConstraintValidatorDescriptor> availableValidators = registrar.getValidatorsAnnotatedInConstraint(constraintClass);

        if (ObjectUtils.isEmpty(availableValidators)) {
            availableValidators = registrar.getAllRegisteredValidatorsForConstraint(constraintClass);
        }

        if (ObjectUtils.isEmpty(availableValidators)) {
            throw new ConstraintValidatorException(
                format("No validator registered for constraint %s", constraintClass.getName())
            );
        }

        final Set<ConstraintValidatorDescriptor> foundValidators = availableValidators.stream()
            .filter(descriptor -> descriptor.canValidate(validatedType))
            .collect(Collectors.toSet());

        final boolean isAnyDuplicateDescriptors = foundValidators.stream().anyMatch(
            validator -> Collections.frequency(foundValidators, validator) > 1
        );

        if (isAnyDuplicateDescriptors) {
            throw new ConstraintValidatorException(
                format("Two or more validators are eligible for constraint %s", constraintClass.getName())
            );
        }

        return foundValidators.stream()
            .sorted(ConstraintValidatorDescriptor.hierarchySortComparator(validatedType))
            .map(descriptor -> (Class<? extends ConstraintValidator<A, ?>>) descriptor.getValidatorImplClass())
            .findFirst()
            .orElseThrow(
                () -> new ConstraintValidatorException( format("Validator not found for type %s", validatedType.getName()) )
            );
    }

    @SuppressWarnings("unchecked")
    public static ConstraintValidator<Annotation, Object> getInitializedConstraintValidator(ConstraintValidatorFactory constraintValidatorFactory, ConstraintDescriptor<Annotation> descriptor) {
        return (ConstraintValidator<Annotation, Object>) descriptor.getConstraintValidatorClasses().stream()
            .map(constraintValidatorFactory::getInstance)
            .peek(validator -> validator.initialize(descriptor.getAnnotation()))
            .findFirst()
            .orElseThrow(() -> null);
    }

}

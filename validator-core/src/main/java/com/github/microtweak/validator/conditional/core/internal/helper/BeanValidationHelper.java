package com.github.microtweak.validator.conditional.core.internal.helper;

import com.github.microtweak.validator.conditional.core.WhenActivatedValidateAs;
import com.github.microtweak.validator.conditional.core.exception.ConstraintValidatorException;
import com.github.microtweak.validator.conditional.core.internal.CvConstraintDescriptorImpl;
import com.github.microtweak.validator.conditional.core.internal.annotated.ValidationPoint;
import com.github.microtweak.validator.conditional.core.spi.BeanValidationImplementationProvider;
import com.github.microtweak.validator.conditional.core.spi.PlatformProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

@Slf4j
@SuppressWarnings("rawtypes")
public final class BeanValidationHelper {

    private static final BeanValidationImplementationProvider implementationHelper = BeanValidationImplementationProvider.getInstance();
    private static final PlatformProvider platformHelper = PlatformProvider.getInstance();

    private static final Map<Class<? extends Annotation>, Set<ConstraintValidatorDescriptor>> constraintValidators = lookupConstraintValidatorsImpl();

    private static Map<Class<? extends Annotation>, Set<ConstraintValidatorDescriptor>> lookupConstraintValidatorsImpl() {
        final Stream<Class<? extends ConstraintValidator>> validatorsStream = implementationHelper.getAvailableConstraintValidators().stream();
        return toConstraintValidatorDescriptorStream(validatorsStream).collect(
            Collectors.groupingBy(
                ConstraintValidatorDescriptor::getAnnotationClass,
                Collectors.toSet()
            )
        );
    }

    private static <A extends Annotation> Set<ConstraintValidatorDescriptor> getValidatorAnnotatedInConstraint(Class<A> constraintClass) {
        final Class<? extends ConstraintValidator<?, ?>>[] validatedBy = constraintClass.getAnnotation(Constraint.class).validatedBy();
        return toConstraintValidatorDescriptorStream(Stream.of(validatedBy)).collect(Collectors.toSet());
    }

    private static Stream<ConstraintValidatorDescriptor> toConstraintValidatorDescriptorStream(Stream<Class<? extends ConstraintValidator>> validatorStream) {
        return validatorStream
            .filter(cls ->
                !Modifier.isInterface(cls.getModifiers()) && !Modifier.isAbstract(cls.getModifiers())
            )
            .map(constraintValidatorClass -> {
                try {
                    return new ConstraintValidatorDescriptor(constraintValidatorClass);
                } catch (TypeNotPresentException e) {
                    log.trace("ConstraintValidator \"{}\" was ignored because type \"{}\" is not present in the classpath", constraintValidatorClass.getName(), e.typeName());
                    return null;
                }
            })
            .filter(
                descriptor -> descriptor != null && descriptor.getAnnotationClass() != null && descriptor.getValidatedClass() != null
            );
    }

    @SuppressWarnings("unchecked")
    public static <A extends Annotation> Class<? extends ConstraintValidator<A, ?>> findConstraintValidatorClass(Class<A> constraintClass, Class<?> validatedType) {
        Set<ConstraintValidatorDescriptor> availableValidators = getValidatorAnnotatedInConstraint(constraintClass);

        if (ObjectUtils.isEmpty(availableValidators)) {
            availableValidators = constraintValidators.get(constraintClass);
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
            .findFirst()
            .map(descriptor -> (Class<? extends ConstraintValidator<A, ?>>) descriptor.getValidatorImplClass())
            .orElseThrow(
                () -> new ConstraintValidatorException( format("Validator not found for type %s", validatedType.getName()) )
            );
    }

    public static Set<ValidationPoint> getAllValidationPointsAt(Class<?> conditionalValidatedClass) {
        return FieldUtils.getAllFieldsList(conditionalValidatedClass).stream()
            .map(ValidationPoint::of)
            .filter(Objects::nonNull)
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static <A extends Annotation> Set<CvConstraintDescriptorImpl<A>> getAllConstraintDescriptorOf(ValidationPoint validationPoint) {
        return validationPoint.getConstraints().stream()
            .map(constraint -> new CvConstraintDescriptorImpl<A>(constraint, validationPoint.getType()))
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @SuppressWarnings("unchecked")
    public static ConstraintValidator<Annotation, Object> getInitializedConstraintValidator(CvConstraintDescriptorImpl<Annotation> descriptor) {
        return (ConstraintValidator<Annotation, Object>) descriptor.getConstraintValidatorClasses().stream()
            .map(platformHelper::getConstraintValidatorInstance)
            .peek(validator -> validator.initialize(descriptor.getAnnotation()))
            .findFirst()
            .orElseThrow(() -> null);
    }

}

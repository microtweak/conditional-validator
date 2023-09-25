package com.github.microtweak.conditionalvalidator.internal.helper;

import lombok.extern.slf4j.Slf4j;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class ConstraintValidatorRegistrar {

    private final Map<Class<? extends Annotation>, Set<ConstraintValidatorDescriptor>> allValidators = new HashMap<>();

    public <CV extends ConstraintValidator<?, ?>> void addValidators(Set<Class<? extends CV>> validators) {
        transformValidatorsClassToDescriptors(validators).forEach(
            descriptor -> allValidators.computeIfAbsent(descriptor.getAnnotationClass(), (k) -> new HashSet<>()).add(descriptor)
        );
    }

    private <CV extends ConstraintValidator<?, ?>> Stream<ConstraintValidatorDescriptor> transformValidatorsClassToDescriptors(Collection<Class<? extends CV>> validators) {
        return validators.stream()
            .filter(
                cls -> !Modifier.isInterface(cls.getModifiers()) && !Modifier.isAbstract(cls.getModifiers()) && !cls.isAnonymousClass()
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

    public Set<ConstraintValidatorDescriptor> getValidatorsAnnotatedInConstraint(Class<? extends Annotation> constraintClass) {
        final Class<? extends ConstraintValidator<?, ?>>[] validatedBy = constraintClass.getAnnotation(Constraint.class).validatedBy();
        return transformValidatorsClassToDescriptors(Arrays.asList(validatedBy)).collect(Collectors.toSet());
    }

    public Set<ConstraintValidatorDescriptor> getAllRegisteredValidatorsForConstraint(Class<? extends Annotation> constraintClass) {
        return allValidators.get(constraintClass);
    }

}
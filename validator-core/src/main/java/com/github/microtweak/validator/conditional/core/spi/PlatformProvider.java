package com.github.microtweak.validator.conditional.core.spi;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import javax.validation.ConstraintValidator;
import javax.validation.ValidatorFactory;
import javax.validation.metadata.ConstraintDescriptor;
import java.lang.annotation.Annotation;
import java.util.Comparator;
import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

import static javax.validation.Validation.buildDefaultValidatorFactory;

public interface PlatformProvider {

    static PlatformProvider getInstance() {
        final Set<PlatformProvider> resolvers = new HashSet<>();

        ServiceLoader.load(PlatformProvider.class).forEach(resolvers::add);

        return resolvers.stream()
            .filter(PlatformProvider::isAvailable)
            .max(Comparator.comparing(PlatformProvider::getOrdinal))
            .orElseThrow(() -> new RuntimeException("No platform found!"));
    }

    int getOrdinal();

    default boolean isAvailable() {
        return true;
    }

    ValidatorFactory getValidatorFactory();

    default <CV extends ConstraintValidator<?, ?>> CV getConstraintValidatorInstance(Class<CV> constraintValidatorClass) {
        return getValidatorFactory().getConstraintValidatorFactory().getInstance(constraintValidatorClass);
    }

    @SuppressWarnings("unchecked")
    default ConstraintValidator<Annotation, Object> getInitializedConstraintValidator(ConstraintDescriptor<Annotation> descriptor) {
        return (ConstraintValidator<Annotation, Object>) descriptor.getConstraintValidatorClasses().stream()
            .map(this::getConstraintValidatorInstance)
            .peek(validator -> validator.initialize(descriptor.getAnnotation()))
            .findFirst()
            .orElseThrow(() -> null);
    }

    class DefaultPlatformHelper implements PlatformProvider {

        private ValidatorFactory validationFactory;

        @Override
        public int getOrdinal() {
            return 0;
        }

        @Override
        public ValidatorFactory getValidatorFactory() {
            if (validationFactory == null) {
                validationFactory = buildDefaultValidatorFactory();
            }
            return validationFactory;
        }

    }

    class CdiPlatformHelper implements PlatformProvider {

        private Instance<ValidatorFactory> validatorFactory;

        @Override
        public int getOrdinal() {
            return 100;
        }

        @Override
        public boolean isAvailable() {
            try {
                if (validatorFactory == null) {
                    validatorFactory = CDI.current().select(ValidatorFactory.class);
                }
                return validatorFactory.isResolvable();
            } catch (Throwable e) {
                return false;
            }
        }

        @Override
        public ValidatorFactory getValidatorFactory() {
            return validatorFactory.get();
        }

    }
}

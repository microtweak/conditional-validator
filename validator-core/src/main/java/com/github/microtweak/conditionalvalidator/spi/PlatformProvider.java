package com.github.microtweak.conditionalvalidator.spi;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.validation.ConstraintValidatorFactory;
import jakarta.validation.MessageInterpolator;
import jakarta.validation.ValidatorFactory;
import java.util.Comparator;
import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;

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

    default ConstraintValidatorFactory getConstraintValidatorFactory() {
        return getValidatorFactory().getConstraintValidatorFactory();
    }

    default MessageInterpolator getMessageInterpolator() {
        return getValidatorFactory().getMessageInterpolator();
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

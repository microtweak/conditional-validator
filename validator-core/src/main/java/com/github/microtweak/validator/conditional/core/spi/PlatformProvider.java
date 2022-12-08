package com.github.microtweak.validator.conditional.core.spi;

import com.github.microtweak.validator.conditional.core.exception.ConstraintValidatorException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;

import javax.enterprise.inject.spi.CDI;
import javax.validation.ConstraintValidator;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

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

    <CV extends ConstraintValidator<?, ?>> CV getConstraintValidatorInstance(Class<CV> constraintValidatorClass);


    class DefaultPlatformHelper implements PlatformProvider {

        @Override
        public int getOrdinal() {
            return 0;
        }

        @Override
        public <CV extends ConstraintValidator<?, ?>> CV getConstraintValidatorInstance(Class<CV> constraintValidatorClass) {
            try {
                return ConstructorUtils.invokeExactConstructor(constraintValidatorClass);
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException e) {
                throw new ConstraintValidatorException("ConstraintValidator does not have a no-args constructor or this constructor is private", e);
            } catch (InvocationTargetException e) {
                return ExceptionUtils.rethrow(e.getTargetException());
            }
        }
    }

    class CdiPlatformHelper implements PlatformProvider {

        @Override
        public int getOrdinal() {
            return 100;
        }

        @Override
        public boolean isAvailable() {
            try {
                CDI.current().getBeanManager();
                return true;
            } catch (Throwable e) {
                return false;
            }
        }

        @Override
        public <CV extends ConstraintValidator<?, ?>> CV getConstraintValidatorInstance(Class<CV> constraintValidatorClass) {
            return CDI.current().select(constraintValidatorClass).get();
        }

    }
}

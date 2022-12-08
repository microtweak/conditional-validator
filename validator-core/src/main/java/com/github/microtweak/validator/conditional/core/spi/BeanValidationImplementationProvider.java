package com.github.microtweak.validator.conditional.core.spi;

import com.github.microtweak.validator.conditional.core.exception.UnsupportedBeanValidatorImplException;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import javax.validation.ConstraintValidator;
import java.util.ServiceLoader;
import java.util.Set;

public interface BeanValidationImplementationProvider {

    static BeanValidationImplementationProvider getInstance() {
        for (BeanValidationImplementationProvider impl : ServiceLoader.load(BeanValidationImplementationProvider.class)) {
            return impl;
        }
        throw new UnsupportedBeanValidatorImplException("No Bean Validation implementation found!");
    }

    ClassLoader[] getClassLoaderOfConstraintValidators();

    String[] getPackagesOfConstraintValidators();

    @SuppressWarnings("rawtypes")
    default Set<Class<? extends ConstraintValidator>> getAvailableConstraintValidators() {
        final Reflections reflections = new Reflections(new ConfigurationBuilder()
            .setClassLoaders( getClassLoaderOfConstraintValidators() )
            .forPackages( getPackagesOfConstraintValidators() )
        );

        return reflections.getSubTypesOf(ConstraintValidator.class);
    }

}

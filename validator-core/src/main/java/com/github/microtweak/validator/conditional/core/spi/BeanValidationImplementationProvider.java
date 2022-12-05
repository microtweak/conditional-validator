package com.github.microtweak.validator.conditional.core.spi;

import com.github.microtweak.validator.conditional.core.exception.UnsupportedBeanValidatorImplException;

import java.util.ServiceLoader;

public interface BeanValidationImplementationProvider {

    static BeanValidationImplementationProvider getInstance() {
        for (BeanValidationImplementationProvider impl : ServiceLoader.load(BeanValidationImplementationProvider.class)) {
            return impl;
        }
        throw new UnsupportedBeanValidatorImplException("No Bean Validation implementation found!");
    }

    ClassLoader[] getClassLoaderOfConstraintValidators();

    String[] getPackagesOfConstraintValidators();

}

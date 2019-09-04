package com.github.microtweak.validator.conditional.core.spi;

import lombok.extern.slf4j.Slf4j;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

@Slf4j
public class NoContainerValidatorFactoryResolver extends ValidatorFactoryResolver {

    private ValidatorFactory factory;

    @Override
    public int getOrdinal() {
        return 100;
    }

    @Override
    public boolean isSupported() {
        tryInitializeFactory();
        return true;
    }

    private synchronized void tryInitializeFactory() {
        if (factory == null) {
            factory = Validation.buildDefaultValidatorFactory();
        }

        if (factory != null) {
            log.trace("No instance of {} found available. Using default instance of Bean Validation Provider...", ValidatorFactory.class.getSimpleName());
        }
    }

    @Override
    public ValidatorFactory getValidatorFactory() {
        return factory;
    }

}

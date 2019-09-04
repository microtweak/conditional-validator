package com.github.microtweak.validator.conditional.cdi;

import com.github.microtweak.validator.conditional.core.spi.ValidatorFactoryResolver;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import javax.validation.ValidatorFactory;

@Slf4j
public class CdiValidatorFactoryResolver extends ValidatorFactoryResolver {

    private Instance<ValidatorFactory> factory;

    @Override
    public int getOrdinal() {
        return 200;
    }

    @Override
    public boolean isSupported() {
        tryLookupFactoryFromCdi();
        return factory != null && factory.isResolvable();
    }

    private synchronized void tryLookupFactoryFromCdi() {
        if (factory == null) {
            factory = CDI.current().select(ValidatorFactory.class);
        }

        if (factory != null && factory.isResolvable()) {
            log.trace("Using {} instance present in CDI", ValidatorFactory.class.getSimpleName());
        }
    }

    @Override
    public ValidatorFactory getValidatorFactory() {
        return factory.get();
    }

}

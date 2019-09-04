package com.github.microtweak.validator.conditional.core.spi;

import javax.validation.ValidatorFactory;
import java.util.Comparator;
import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

public abstract class ValidatorFactoryResolver {

    private static ValidatorFactoryResolver INSTANCE;

    public static ValidatorFactoryResolver getInstance() {
        if (INSTANCE == null) {
            synchronized (ValidatorFactoryResolver.class) {
                Set<ValidatorFactoryResolver> resolvers = new HashSet<>();

                ServiceLoader.load(ValidatorFactoryResolver.class).forEach(resolvers::add);

                INSTANCE = resolvers.stream()
                        .sorted( Comparator.comparing( ValidatorFactoryResolver::getOrdinal ).reversed() )
                        .filter(ValidatorFactoryResolver::isSupported)
                        .findFirst()
                        .get();
            }
        }
        return INSTANCE;
    }

    public abstract int getOrdinal();

    public abstract boolean isSupported();

    public abstract ValidatorFactory getValidatorFactory();

}
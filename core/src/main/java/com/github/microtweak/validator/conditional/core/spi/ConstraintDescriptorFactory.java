package com.github.microtweak.validator.conditional.core.spi;

import com.github.microtweak.validator.conditional.core.exception.UnsupportedBeanValidatorImplException;

import javax.validation.metadata.ConstraintDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ServiceLoader;

public abstract class ConstraintDescriptorFactory {

    private static ConstraintDescriptorFactory INSTANCE;

    public static ConstraintDescriptorFactory getInstance() {
        if (INSTANCE == null) {
            synchronized (ConstraintDescriptorFactory.class) {
                for (ConstraintDescriptorFactory impl : ServiceLoader.load(ConstraintDescriptorFactory.class)) {
                    INSTANCE = impl;
                    break;
                }

                if (INSTANCE == null) {
                    throw new UnsupportedBeanValidatorImplException("No provider of ConditionalValidator found for implementation of BeanValidation");
                }
            }
        }
        return INSTANCE;
    }

    public abstract  <T extends Annotation, H extends AnnotatedElement> ConstraintDescriptor<T> of(T constraint, H host);

}

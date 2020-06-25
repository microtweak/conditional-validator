package com.github.microtweak.validator.conditional.hv;

import com.github.microtweak.validator.conditional.core.spi.ConstraintDescriptorFactory;
import com.github.microtweak.validator.conditional.hv.internal.ConstraintDescriptorImplStrategy;
import com.github.microtweak.validator.conditional.hv.internal.Hv60XConstraintDescriptorImplStrategy;
import com.github.microtweak.validator.conditional.hv.internal.Hv61XConstraintDescriptorImplStrategy;

import javax.validation.metadata.ConstraintDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public class HibernateValidatorConstraintDescriptorFactory extends ConstraintDescriptorFactory {

    private ConstraintDescriptorImplStrategy strategy;

    public HibernateValidatorConstraintDescriptorFactory() {
        final VersionInfo version = VersionInfo.getCurrent();

        try {
            if (version.gt("6.0.20")) {
                strategy = new Hv61XConstraintDescriptorImplStrategy();
            } else if (version.ge("6.0.6") && version.le("6.0.20")) {
                strategy = new Hv60XConstraintDescriptorImplStrategy();
            } else {
                throw new IllegalStateException("Hibernate Validator older then 6.0.6 is not supported");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T extends Annotation, H extends AnnotatedElement> ConstraintDescriptor<T> of(T constraint, H host) {
        return strategy.newInstance(constraint, host);
    }

}

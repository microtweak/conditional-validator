package com.github.microtweak.validator.conditional.bval;

import com.github.microtweak.validator.conditional.core.spi.ConstraintDescriptorFactory;
import com.github.microtweak.validator.conditional.core.spi.ValidatorFactoryResolver;
import org.apache.bval.jsr.ApacheValidatorFactory;
import org.apache.bval.jsr.descriptor.ConstraintD;
import org.apache.bval.jsr.metadata.Meta;

import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.Scope;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

public class ApacheBValConstraintDescriptorFactory extends ConstraintDescriptorFactory {

    private ApacheValidatorFactory factory;

    @Override
    public <T extends Annotation, H extends AnnotatedElement> ConstraintDescriptor<T> of(T constraint, H host) {
        return new ConstraintD<>(constraint, Scope.LOCAL_ELEMENT, metaOf( host ), getFactory());
    }

    private <H extends AnnotatedElement> Meta<?> metaOf(H host) {
        if (host instanceof Field) {
            return new Meta.ForField( Field.class.cast(host) );
        }

        return null;
    }

    private ApacheValidatorFactory getFactory() {
        if (factory == null) {
            factory = (ApacheValidatorFactory) ValidatorFactoryResolver.getInstance().getValidatorFactory();
        }
        return factory;
    }

}

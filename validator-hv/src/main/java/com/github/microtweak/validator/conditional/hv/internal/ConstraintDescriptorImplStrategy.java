package com.github.microtweak.validator.conditional.hv.internal;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
import org.hibernate.validator.internal.util.annotation.ConstraintAnnotationDescriptor;

import javax.validation.metadata.ConstraintDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;

public abstract class ConstraintDescriptorImplStrategy {

    protected ConstraintHelper constraintHelper;
    protected Constructor<ConstraintDescriptorImpl> constructor;

    public <T extends Annotation, H extends AnnotatedElement> ConstraintDescriptor<T> newInstance(T constraint, H host) {
        final ConstraintAnnotationDescriptor constraintDescriptor = new ConstraintAnnotationDescriptor.Builder(constraint).build();

        try {
            return constructor.newInstance(constraintHelper, getMember(host), constraintDescriptor, getElementTypeOf(host));
        } catch (ReflectiveOperationException e) {
            return ExceptionUtils.rethrow(e);
        }
    }

    protected abstract <H extends AnnotatedElement> Object getMember(H host) throws ReflectiveOperationException;

    protected abstract <H extends AnnotatedElement> Object getElementTypeOf(H host);

}

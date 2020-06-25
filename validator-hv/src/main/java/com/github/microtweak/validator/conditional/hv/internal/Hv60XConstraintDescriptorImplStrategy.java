package com.github.microtweak.validator.conditional.hv.internal;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
import org.hibernate.validator.internal.util.annotation.ConstraintAnnotationDescriptor;

import java.lang.annotation.ElementType;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;

/**
 * Strategy for obtaining ConstraintDescriptorImpl from HV 6.0.6 ~ 6.0.20
 */
public class Hv60XConstraintDescriptorImplStrategy extends ConstraintDescriptorImplStrategy {

    public Hv60XConstraintDescriptorImplStrategy() {
        try {
            constraintHelper = ConstructorUtils.invokeConstructor(ConstraintHelper.class);

            final Class<?>[] parameterTypes = {
                ConstraintHelper.class, Member.class, ConstraintAnnotationDescriptor.class, ElementType.class
            };

            constructor = ConstructorUtils.getAccessibleConstructor(ConstraintDescriptorImpl.class, parameterTypes);
        } catch (ReflectiveOperationException e) {
            ExceptionUtils.rethrow(e);
        }
    }

    @Override
    protected <H extends AnnotatedElement> Member getMember(H host) {
        if (host instanceof Field) {
            return (Field) host;
        }
        return null;
    }

    @Override
    protected <H extends AnnotatedElement> ElementType getElementTypeOf(H host) {
        if (host instanceof Field) {
            return ElementType.LOCAL_VARIABLE;
        }
        return null;
    }

}

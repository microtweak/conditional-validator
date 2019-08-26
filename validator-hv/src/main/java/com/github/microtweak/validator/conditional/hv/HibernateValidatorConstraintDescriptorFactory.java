package com.github.microtweak.validator.conditional.hv;

import com.github.microtweak.validator.conditional.core.spi.ConstraintDescriptorFactory;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
import org.hibernate.validator.internal.util.annotation.ConstraintAnnotationDescriptor;

import javax.validation.metadata.ConstraintDescriptor;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;

public class HibernateValidatorConstraintDescriptorFactory extends ConstraintDescriptorFactory {

    private static final ConstraintHelper CONSTRAINT_HELPER = new ConstraintHelper();

    @Override
    public <T extends Annotation, H extends AnnotatedElement> ConstraintDescriptor<T> of(T constraint, H host) {
        ConstraintAnnotationDescriptor constraintDescriptor = new ConstraintAnnotationDescriptor.Builder(constraint).build();
        return new ConstraintDescriptorImpl(CONSTRAINT_HELPER, getMember( host ), constraintDescriptor, getElementTypeOf( host ));
    }

    private <H extends AnnotatedElement> Member getMember(H host) {
        if (host instanceof Field) {
            return (Field) host;
        }
        return null;
    }

    private <H extends AnnotatedElement> ElementType getElementTypeOf(H host) {
        if (host instanceof Field) {
            return ElementType.LOCAL_VARIABLE;
        }
        return null;
    }

}

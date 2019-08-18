package com.github.microtweak.validator.conditional.core.hv;

import com.github.microtweak.validator.conditional.core.spi.ConstraintDescriptorFactory;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
import org.hibernate.validator.internal.util.annotation.ConstraintAnnotationDescriptor;

import javax.validation.metadata.ConstraintDescriptor;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;

public class HibernateValidatorConstraintDescriptorFactory extends ConstraintDescriptorFactory {

    private static final ConstraintHelper CONSTRAINT_HELPER = new ConstraintHelper();

    @Override
    public <T extends Annotation> ConstraintDescriptor<T> of(T constraint) {
        ConstraintAnnotationDescriptor constraintDescriptor = new ConstraintAnnotationDescriptor.Builder(constraint).build();
        return new ConstraintDescriptorImpl(CONSTRAINT_HELPER, null, constraintDescriptor, ElementType.LOCAL_VARIABLE);
    }

}

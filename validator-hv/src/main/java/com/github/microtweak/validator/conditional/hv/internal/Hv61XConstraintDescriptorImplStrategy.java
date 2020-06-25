package com.github.microtweak.validator.conditional.hv.internal;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
import org.hibernate.validator.internal.util.annotation.ConstraintAnnotationDescriptor;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * Strategy for obtaining ConstraintDescriptorImpl from HV 6.1.x
 */
public class Hv61XConstraintDescriptorImplStrategy extends ConstraintDescriptorImplStrategy {

    private Class<Enum> constraintLocationKindClass;
    private Class<?> javaBeanFieldConstrainableClass;

    public Hv61XConstraintDescriptorImplStrategy() {
        try {
            constraintHelper = newConstraintHelper();

            constraintLocationKindClass = (Class<Enum>) Class.forName("org.hibernate.validator.internal.metadata.location.ConstraintLocation$ConstraintLocationKind");

            javaBeanFieldConstrainableClass = Class.forName("org.hibernate.validator.internal.properties.javabean.JavaBeanField");

            final Class<?>[] parameterTypes = {
                ConstraintHelper.class,
                Class.forName("org.hibernate.validator.internal.properties.Constrainable"),
                ConstraintAnnotationDescriptor.class,
                constraintLocationKindClass
            };

            constructor = ConstructorUtils.getAccessibleConstructor(ConstraintDescriptorImpl.class, parameterTypes);
        } catch (ReflectiveOperationException e) {
            ExceptionUtils.rethrow(e);
        }
    }

    private ConstraintHelper newConstraintHelper() throws ReflectiveOperationException {
        Constructor<ConstraintHelper> noArgsConstructor = ConstructorUtils.getAccessibleConstructor(ConstraintHelper.class);
        if (noArgsConstructor != null) {
            return noArgsConstructor.newInstance();
        }

        return (ConstraintHelper) MethodUtils.invokeExactStaticMethod(ConstraintHelper.class, "forAllBuiltinConstraints");
    }

    @Override
    protected <H extends AnnotatedElement> Object getMember(H host) throws ReflectiveOperationException {
        if (host instanceof Field) {
            Field field = (Field) host;
            return ConstructorUtils.invokeConstructor(javaBeanFieldConstrainableClass, field, field.getName());
        }

        return null;
    }

    @Override
    protected <H extends AnnotatedElement> Object getElementTypeOf(H host) {
        if (host instanceof Field) {
            return Enum.valueOf(constraintLocationKindClass, "FIELD");
        }
        return null;
    }
}

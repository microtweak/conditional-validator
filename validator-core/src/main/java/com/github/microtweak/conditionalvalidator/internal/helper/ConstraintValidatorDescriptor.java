package com.github.microtweak.conditionalvalidator.internal.helper;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.ClassUtils;

import jakarta.validation.ConstraintValidator;
import java.lang.annotation.Annotation;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.String.format;
import static org.apache.commons.lang3.reflect.TypeUtils.getRawType;

@EqualsAndHashCode(of = { "annotationClass", "validatedClass" })
@ToString
@Getter
class ConstraintValidatorDescriptor {

    private static final TypeVariable<?> annotationTypeGeneric = ConstraintValidator.class.getTypeParameters()[0];
    private static final TypeVariable<?> validatedTypeGeneric = ConstraintValidator.class.getTypeParameters()[1];

    private final Class<? extends Annotation> annotationClass;

    private final Class<?> validatedClass;
    private final Class<? extends ConstraintValidator<?, ?>> validatorImplClass;

    @SuppressWarnings("unchecked")
    public ConstraintValidatorDescriptor(Class<? extends ConstraintValidator<?, ?>> validatorImplClass) {
        this.annotationClass = (Class<? extends Annotation>) getRawType(annotationTypeGeneric, validatorImplClass);
        this.validatedClass =  getRawType(validatedTypeGeneric, validatorImplClass);
        this.validatorImplClass = validatorImplClass;
    }

    public boolean canValidate(Class<?> cls) {
        return ClassUtils.isAssignable(cls, getValidatedClass());
    }

    public static Comparator<ConstraintValidatorDescriptor> hierarchySortComparator(Class<?> validatedType) {
        return (left, right) -> {
            final int distanciaLeft = calculateDistanceToAncestor(validatedType, left.validatedClass);
            final int distanciaRight = calculateDistanceToAncestor(validatedType, right.validatedClass);
            return Integer.compare(distanciaLeft, distanciaRight);
        };
    }

    private static int calculateDistanceToAncestor(Class<?> descendant, Class<?> ancestor) {
        descendant = ClassUtils.primitiveToWrapper(descendant);
        ancestor = ClassUtils.primitiveToWrapper(ancestor);

        if (!ClassUtils.isAssignable(descendant, ancestor)) {
            throw new IllegalArgumentException(
                format("Type \"%s\" does not descend from \"%s\"", descendant.getName(), ancestor.getName())
            );
        }

        final List<Class<?>> allAncestors = new ArrayList<>();
        ClassUtils.hierarchy(descendant, ClassUtils.Interfaces.INCLUDE).forEach(allAncestors::add);

        if (descendant.isInterface()) {
            allAncestors.add(Object.class);
        }

        return allAncestors.indexOf(ancestor);
    }

}

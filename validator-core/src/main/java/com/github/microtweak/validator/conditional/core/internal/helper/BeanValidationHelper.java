package com.github.microtweak.validator.conditional.core.internal.helper;

import com.github.microtweak.validator.conditional.core.WhenActivatedValidateAs;
import com.github.microtweak.validator.conditional.core.exception.ConstraintValidatorException;
import com.github.microtweak.validator.conditional.core.internal.ConditinalDescriptor;
import com.github.microtweak.validator.conditional.core.internal.annotated.ConstraintTarget;
import com.github.microtweak.validator.conditional.core.spi.BeanValidationImplementationProvider;
import com.github.microtweak.validator.conditional.core.spi.PlatformProvider;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.lang.reflect.TypeVariable;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static org.apache.commons.lang3.reflect.TypeUtils.getRawType;

@Slf4j
@SuppressWarnings("rawtypes")
public final class BeanValidationHelper {

    private static final BeanValidationImplementationProvider implementationHelper = BeanValidationImplementationProvider.getInstance();
    private static final PlatformProvider platformHelper = PlatformProvider.getInstance();

    private static final Map<Class<? extends Annotation>, Set<ConstraintValidatorDescriptor>> constraintValidators = lookupConstraintValidatorsImpl();

    private static Map<Class<? extends Annotation>, Set<ConstraintValidatorDescriptor>> lookupConstraintValidatorsImpl() {
        final Stream<Class<? extends ConstraintValidator>> validatorsStream = implementationHelper.getAvailableConstraintValidators().stream();
        return toConstraintValidatorDescriptorStream(validatorsStream).collect(
            Collectors.groupingBy(
                ConstraintValidatorDescriptor::getAnnotationClass,
                Collectors.toSet()
            )
        );
    }

    private static <A extends Annotation> Set<ConstraintValidatorDescriptor> getValidatorAnnotatedInConstraint(Class<A> constraintClass) {
        final Class<? extends ConstraintValidator<?, ?>>[] validatedBy = constraintClass.getAnnotation(Constraint.class).validatedBy();
        return toConstraintValidatorDescriptorStream(Stream.of(validatedBy)).collect(Collectors.toSet());
    }

    private static Stream<ConstraintValidatorDescriptor> toConstraintValidatorDescriptorStream(Stream<Class<? extends ConstraintValidator>> validatorStream) {
        return validatorStream
            .filter(cls ->
                !Modifier.isInterface(cls.getModifiers()) && !Modifier.isAbstract(cls.getModifiers())
            )
            .map(constraintValidatorClass -> {
                try {
                    return new ConstraintValidatorDescriptor(constraintValidatorClass);
                } catch (TypeNotPresentException e) {
                    log.trace("ConstraintValidator \"{}\" was ignored because type \"{}\" is not present in the classpath", constraintValidatorClass.getName(), e.typeName());
                    return null;
                }
            })
            .filter(descriptor -> descriptor != null && descriptor.isValid());
    }

    public static Annotation getActualBeanValidationContraintOf(Annotation conditionalConstraint) {
        final Class<? extends Annotation> actualConstraintType = conditionalConstraint.annotationType().getAnnotation(WhenActivatedValidateAs.class).value();

        if (actualConstraintType == null) {
            throw new IllegalArgumentException(
                format("Conditional constraint is not annotated with %s!", WhenActivatedValidateAs.class)
            );
        }

        final Map<String, Object> attributes = AnnotationHelper.readAllAtributeExcept(conditionalConstraint, "expression");
        return AnnotationHelper.createAnnotation(actualConstraintType, attributes);
    }

    public static Class<? extends ConstraintValidator> findConstraintValidatorClass(Class<? extends Annotation> constraintClass, Class<?> validatedType) {
        Set<ConstraintValidatorDescriptor> availableValidators = getValidatorAnnotatedInConstraint(constraintClass);

        if (ObjectUtils.isEmpty(availableValidators)) {
            availableValidators = constraintValidators.get(constraintClass);
        }

        if (ObjectUtils.isEmpty(availableValidators)) {
            throw new ConstraintValidatorException(
                format("No validator registered for constraint %s", constraintClass.getName())
            );
        }

        final Set<ConstraintValidatorDescriptor> foundValidators = availableValidators.stream()
            .filter(descriptor -> descriptor.getValidatedClass().isAssignableFrom(validatedType))
            .collect(Collectors.toSet());

        if (foundValidators.size() > 1) {
            throw new ConstraintValidatorException(
                format("Two or more validators are eligible for constraint %s", constraintClass.getName())
            );
        }

        return foundValidators.stream()
            .findFirst()
            .map(ConstraintValidatorDescriptor::getValidatorImplClass)
            .orElseThrow(
                () -> new ConstraintValidatorException( format("Validator not found for type %s", validatedType.getName()) )
            );
    }

    public static Set<ConditinalDescriptor> getConditinalDescriptorOf(Class<?> conditionalValidatedClass) {
        return FieldUtils.getAllFieldsList(conditionalValidatedClass).stream()
            .map(ConstraintTarget::of)
            .filter(Objects::nonNull)
            .flatMap(target -> target.getConstraints().stream()
                .map(constraint -> new ConditinalDescriptor(target, constraint))
            )
            .collect(Collectors.toSet());
    }

    @SuppressWarnings("unchecked")
    public static boolean invokeConstraintValidator(Object validatedBean, ConditinalDescriptor descriptor, ConstraintValidatorContext context) {
        final Object value = descriptor.getConstraintTarget().getTargetValue(validatedBean);

        final ConstraintValidator<Annotation, Object> validator = platformHelper.getConstraintValidatorInstance(descriptor.getValidatorClass());
        validator.initialize(descriptor.getActualConstraint());

        return validator.isValid(value, context);
    }

    @ToString
    @Getter
    @SuppressWarnings("rawtypes")
    static class ConstraintValidatorDescriptor {

        private static final TypeVariable<?> annotationTypeGeneric = ConstraintValidator.class.getTypeParameters()[0];
        private static final TypeVariable<?> validatedTypeGeneric = ConstraintValidator.class.getTypeParameters()[1];

        private final Class<? extends Annotation> annotationClass;

        private final Class<?> validatedClass;
        private final Class<? extends ConstraintValidator> validatorImplClass;

        @SuppressWarnings("unchecked")
        ConstraintValidatorDescriptor(Class<? extends ConstraintValidator> validatorImplClass) {
            this.annotationClass = (Class<? extends Annotation>) getRawType(annotationTypeGeneric, validatorImplClass);
            this.validatedClass =  getRawType(validatedTypeGeneric, validatorImplClass);
            this.validatorImplClass = validatorImplClass;
        }

        public boolean isValid() {
            return annotationClass != null && validatedClass != null;
        }

    }

}

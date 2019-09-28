package com.github.microtweak.validator.conditional.core.internal;

import com.github.microtweak.validator.conditional.core.ConditionalConstraint;
import com.github.microtweak.validator.conditional.core.spi.ConstraintDescriptorFactory;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.TypeUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorFactory;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.TypeVariable;
import java.util.Map;

import static com.github.microtweak.validator.conditional.core.internal.AnnotationHelper.readAttribute;

@ToString(of = { "name", "expression", "actualConstraint" })
public class ConditionalDescriptor {

    private Field field;

    @Getter
    private String expression;

    @Getter
    private Annotation actualConstraint;

    @Getter
    private String constraintMessage;

    private ConstraintValidator validator;

    public ConditionalDescriptor(Field field, Annotation conditional) {
        this.field = field;
        field.setAccessible(true);

        try {
            expression = readAttribute(conditional, "expression", String.class);
            Validate.notEmpty(expression);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("The conditional constraint " + conditional.annotationType() + " does not have the attribute \"expression\"!");
        }

        Class<? extends Annotation> actualConstraintType = conditional.annotationType().getAnnotation(ConditionalConstraint.class).value();

        if (actualConstraintType == null) {
            throw new IllegalArgumentException("Conditional constraint is not annotated with " + ConditionalConstraint.class + "!");
        }

        actualConstraint = createConstraintBy(conditional, actualConstraintType);

        try {
            constraintMessage = readAttribute(actualConstraint, "message", String.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("The Bean Validation constraint " + actualConstraint.annotationType() + " does not have the attribute \"message\"!");
        }
    }

    public String getName() {
        return field.getName();
    }

    private Annotation createConstraintBy(Annotation conditionalConstraint, Class<? extends Annotation> targetConstraint) {
        Map<String, Object> attributes = AnnotationHelper.readAllAtributeExcept(conditionalConstraint, "expression");
        return AnnotationHelper.createAnnotation(targetConstraint, attributes);
    }

    public void initialize(ConstraintValidatorFactory factory) {
        Class<? extends ConstraintValidator> validatorClass = ConstraintDescriptorFactory.getInstance().of(actualConstraint, field)
                .getConstraintValidatorClasses().stream()
                .filter(this::isCapableValidator)
                .findFirst()
                .orElse( null );

        validator = factory.getInstance( validatorClass );
        validator.initialize( actualConstraint );
    }

    private boolean isCapableValidator(Class<? extends ConstraintValidator<?, ?>> clazz) {
        TypeVariable<?> typeVar = ConstraintValidator.class.getTypeParameters()[1];
        return TypeUtils.getRawType(typeVar, clazz).isAssignableFrom( field.getType() );
    }

    public boolean isValid(Object bean, ConstraintValidatorContext context) {
        Object value = null;

        try {
            value = field.get(bean);
        } catch (IllegalAccessException e) {
            ExceptionUtils.rethrow(e);
        }

        return validator.isValid(value, context);
    }

}

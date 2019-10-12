package com.github.microtweak.validator.conditional.core;

import com.github.microtweak.validator.conditional.core.internal.ConditionalDescriptor;
import com.github.microtweak.validator.conditional.core.spi.ValidatorFactoryResolver;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidatorFactory;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.microtweak.validator.conditional.core.internal.AnnotationHelper.getAnnotationsWithAnnotation;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Slf4j
public class DelegatedConditionalConstraintValidator implements ConstraintValidator<ConditionalValidate, Object> {

    private ValidatorFactory factory;
    private ExpressionEvaluator evaluator;

    @Override
    public void initialize(ConditionalValidate conditionalConstraint) {
        log.trace("Initializing {} for {}", getClass().getSimpleName(), conditionalConstraint.annotationType());

        factory = ValidatorFactoryResolver.getInstance().getValidatorFactory();
        evaluator = new ExpressionEvaluator( conditionalConstraint.contextClasses() );
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        List<ConditionalDescriptor> conditionalDescriptors = findAllConditionalConstraints(value.getClass());

        context.disableDefaultConstraintViolation();

        boolean isValid = true;

        for (ConditionalDescriptor descriptor : conditionalDescriptors) {
            if (!evaluator.isTrueExpression(value, descriptor.getExpression())) {
                continue;
            }

            if (!descriptor.isValid(value, context)) {
                isValid = false;

                context.buildConstraintViolationWithTemplate( descriptor.getConstraintMessage() )
                        .addPropertyNode( descriptor.getName() )
                        .addConstraintViolation();
            }
        }

        return isValid;
    }

    private List<ConditionalDescriptor> findAllConditionalConstraints(Class<?> conditionalContextType) {
        return FieldUtils.getAllFieldsList( conditionalContextType ).stream()
                .flatMap(f -> {
                    Annotation[] conditionalConstraints = getAnnotationsWithAnnotation(f, WhenActivatedValidateAs.class);

                    if (isEmpty( conditionalConstraints )) {
                        return Stream.empty();
                    }

                    return Stream.of( conditionalConstraints ).map(a -> new ConditionalDescriptor(f, a));
                })
                .peek(descriptor -> descriptor.initialize(factory.getConstraintValidatorFactory()))
                .collect(Collectors.toList());
    }

}

package com.github.microtweak.validator.conditional.core;

import com.github.microtweak.validator.conditional.core.internal.ConditionalDescriptor;
import com.github.microtweak.validator.conditional.core.spi.ValidatorFactoryResolver;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;

import javax.el.ELProcessor;
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
    private ELProcessor elProcessor;

    private List<ConditionalDescriptor> conditionalDescriptors;

    @Override
    public void initialize(ConditionalValidate conditionalConstraint) {
        log.trace("Initializing {} for {}", getClass().getSimpleName(), conditionalConstraint.annotationType());

        factory = ValidatorFactoryResolver.getInstance().getValidatorFactory();
        elProcessor = new ELProcessor();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        findAllConditionalConstraints(value.getClass());

        context.disableDefaultConstraintViolation();

        elProcessor.getELManager().defineBean("this", value);

        boolean isValid = true;

        for (ConditionalDescriptor descriptor : conditionalDescriptors) {
            if (!descriptor.isConstraintEnabled(elProcessor)) {
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

    private void findAllConditionalConstraints(Class<?> conditionalContextType) {
        if (conditionalDescriptors != null) {
            return;
        }

        conditionalDescriptors = FieldUtils.getAllFieldsList( conditionalContextType ).parallelStream()
                .flatMap(f -> {
                    Annotation[] conditionalConstraints = getAnnotationsWithAnnotation(f, ConditionalConstraint.class);

                    if (isEmpty( conditionalConstraints )) {
                        return Stream.empty();
                    }

                    return Stream.of( conditionalConstraints ).map(a -> new ConditionalDescriptor(f, a));
                })
                .peek(descriptor -> descriptor.initialize(factory.getConstraintValidatorFactory()))
                .collect(Collectors.toList());
    }

}

package com.github.microtweak.validator.conditional.core;

import com.github.microtweak.validator.conditional.core.internal.ConditionalDescriptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;

import javax.el.ELProcessor;
import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.microtweak.validator.conditional.core.internal.AnnotationHelper.getAnnotationsWithAnnotation;
import static com.github.microtweak.validator.conditional.core.internal.AnnotationHelper.hasAnyAnnotationWithAnnotation;

@Slf4j
public class ConditionalValidateConstraintValidator implements ConstraintValidator<ConditionalValidate, Object> {

    @Inject
    private ValidatorFactory factory;

    private ELProcessor elProcessor;

    private List<ConditionalDescriptor> conditionalDescriptors;

    @Override
    public void initialize(ConditionalValidate conditionalConstraint) {
        log.trace("Initializing {} for {}", getClass().getSimpleName(), conditionalConstraint.annotationType());

        if (factory == null) {
            log.debug("No ValidatorFactory injected, building default one");
            factory = Validation.buildDefaultValidatorFactory();
        }

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
                .filter(f -> hasAnyAnnotationWithAnnotation(f, ConditionalConstraint.class))
                .flatMap(f -> {
                    Annotation[] conditionalConstraints = getAnnotationsWithAnnotation(f, ConditionalConstraint.class);
                    return Stream.of( conditionalConstraints ).map(a -> new ConditionalDescriptor(f, a));
                })
                .peek(descriptor -> descriptor.initialize(factory.getConstraintValidatorFactory()))
                .collect(Collectors.toList());
    }

}

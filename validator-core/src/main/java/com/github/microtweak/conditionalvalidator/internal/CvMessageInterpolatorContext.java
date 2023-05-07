package com.github.microtweak.conditionalvalidator.internal;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.MessageInterpolator;
import javax.validation.ValidationException;
import javax.validation.metadata.ConstraintDescriptor;

import static java.lang.String.format;

@Getter
@AllArgsConstructor
public class CvMessageInterpolatorContext implements MessageInterpolator.Context {

    private final ConstraintDescriptor<?> constraintDescriptor;
    private final Object validatedValue;

    @Override
    public <T> T unwrap(Class<T> type) {
        if (type.isAssignableFrom(CvMessageInterpolatorContext.class)) {
            return type.cast(this);
        }
        throw new ValidationException( format("Type %s not supported for unwrapping.", type) );
    }

}
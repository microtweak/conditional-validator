package com.github.microtweak.conditionalvalidator.hv;

import com.github.microtweak.conditionalvalidator.spi.BeanValidationImplementationProvider;
import lombok.Getter;
import org.hibernate.validator.HibernateValidator;

@Getter
public class HVBeanValidationImplementationHelper implements BeanValidationImplementationProvider {

    private final ClassLoader[] classLoaderOfConstraintValidators = {
        HibernateValidator.class.getClassLoader()
    };

    private final String[] packagesOfConstraintValidators = {
        "org.hibernate.validator.internal.constraintvalidators.bv",
        "org.hibernate.validator.internal.constraintvalidators.hv"
    };

}

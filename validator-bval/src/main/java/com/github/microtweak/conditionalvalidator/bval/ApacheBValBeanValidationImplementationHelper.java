package com.github.microtweak.conditionalvalidator.bval;

import com.github.microtweak.conditionalvalidator.spi.BeanValidationImplementationProvider;
import lombok.Getter;
import org.apache.bval.jsr.ApacheValidatorFactory;

@Getter
public class ApacheBValBeanValidationImplementationHelper implements BeanValidationImplementationProvider {

    private final ClassLoader[] classLoaderOfConstraintValidators = {
        ApacheValidatorFactory.class.getClassLoader()
    };

    private final String[] packagesOfConstraintValidators = {
        "org.apache.bval.constraints",
        "org.apache.bval.extras.constraints"
    };

}

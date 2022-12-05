package com.github.microtweak.validator.conditional.bval;

import com.github.microtweak.validator.conditional.core.spi.BeanValidationImplementationProvider;
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

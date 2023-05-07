package com.github.microtweak.conditionalvalidator.bean;

import com.github.microtweak.conditionalvalidator.ConditionalValidate;
import com.github.microtweak.conditionalvalidator.constraint.NotEmptyWhen;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConditionalValidate
public class Address {

    private boolean applyValidation;

    @NotEmptyWhen(expression = "applyValidation")
    private String street;

}

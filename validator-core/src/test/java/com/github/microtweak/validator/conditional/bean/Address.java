package com.github.microtweak.validator.conditional.bean;

import com.github.microtweak.validator.conditional.core.ConditionalValidate;
import com.github.microtweak.validator.conditional.core.constraint.NotEmptyWhen;
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

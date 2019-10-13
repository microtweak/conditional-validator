package com.github.microtweak.validator.conditional.bean;

import com.github.microtweak.validator.conditional.core.ConditionalValidate;
import com.github.microtweak.validator.conditional.core.constraint.NotNullWhen;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ConditionalValidate
public class Person {

    private boolean applyCascade;

    @NotNull
    private String name;

    @Valid
    @NotNullWhen(expression = "applyCascade")
    private Address address;

}

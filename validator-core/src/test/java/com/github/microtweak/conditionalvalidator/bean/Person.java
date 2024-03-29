package com.github.microtweak.conditionalvalidator.bean;

import com.github.microtweak.conditionalvalidator.ConditionalValidate;
import com.github.microtweak.conditionalvalidator.constraint.NotNullWhen;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

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

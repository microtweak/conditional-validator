package com.github.microtweak.validator.conditional.tests;

import com.github.microtweak.validator.conditional.bean.Address;
import com.github.microtweak.validator.conditional.bean.Person;
import com.github.microtweak.validator.conditional.junit5.ProviderTest;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;

@ProviderTest
public class CascadeValidationTest extends BaseConstraintViolationTest {

    @Test
    public void firstLevelOfGraph() {
        Person p = new Person();

        Set<ConstraintViolation<Person>> violation = applyValidation(p);

        assertAll(
                () -> assertHasPropertyViolation(violation, "name"),
                () -> assertHasNotPropertyViolation(violation, "address")
        );

        p.setApplyCascade(true);

        violation.clear();
        violation.addAll( applyValidation(p) );

        assertAll(
                () -> assertHasPropertyViolation(violation, "name"),
                () -> assertHasPropertyViolation(violation, "address")
        );
    }

    @Test
    public void secondLevelOfGraph() {
        Person p = new Person();
        p.setAddress(new Address());

        Set<ConstraintViolation<Person>> violation = applyValidation(p);

        assertHasNotPropertyViolation(violation, "address.street");

        p.getAddress().setApplyValidation(true);
        violation = applyValidation(p);

        assertHasPropertyViolation(violation, "address.street");
    }

}

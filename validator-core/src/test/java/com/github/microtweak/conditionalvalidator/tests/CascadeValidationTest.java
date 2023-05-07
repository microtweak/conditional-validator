package com.github.microtweak.conditionalvalidator.tests;

import com.github.microtweak.conditionalvalidator.tags.ProviderTest;
import com.github.microtweak.conditionalvalidator.bean.Address;
import com.github.microtweak.conditionalvalidator.bean.Person;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;

@ProviderTest
public class CascadeValidationTest extends BaseBvIntegrationTest {

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

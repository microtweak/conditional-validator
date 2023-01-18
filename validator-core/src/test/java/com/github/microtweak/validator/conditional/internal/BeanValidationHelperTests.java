package com.github.microtweak.validator.conditional.internal;

import com.github.microtweak.validator.conditional.bean.Address;
import com.github.microtweak.validator.conditional.core.constraint.EmailWhen;
import com.github.microtweak.validator.conditional.core.constraint.NotNullWhen;
import com.github.microtweak.validator.conditional.core.exception.ConstraintValidatorException;
import com.github.microtweak.validator.conditional.core.internal.ConditinalDescriptor;
import com.github.microtweak.validator.conditional.core.internal.helper.BeanValidationHelper;
import com.github.microtweak.validator.conditional.internal.constraint.FakeConstraint;
import com.github.microtweak.validator.conditional.internal.constraint.FakeConstraintCharSequenceValidator;
import com.github.microtweak.validator.conditional.internal.constraint.FakeConstraintStringValidator;
import com.github.microtweak.validator.conditional.internal.literal.ConstraintLiteral;
import com.github.microtweak.validator.conditional.junit5.ProviderTest;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.*;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.*;

@ProviderTest
public class BeanValidationHelperTests {

    @Test
    public void extractActualBeanValidationContraint() {
        final NotNullWhen notNullWhen = new ConstraintLiteral<>(NotNullWhen.class).build();
        final Annotation actualNotNull = BeanValidationHelper.getActualBeanValidationContraintOf(notNullWhen);

        assertAll(
            () -> assertNotNull(actualNotNull),
            () -> assertEquals(NotNull.class, actualNotNull.annotationType())
        );

        final EmailWhen emailWhen = new ConstraintLiteral<>(EmailWhen.class)
                .attribute("regexp", ".*")
                .attribute("flags", new Pattern.Flag[0])
                .build();
        final Annotation actualEmail = BeanValidationHelper.getActualBeanValidationContraintOf(emailWhen);

        assertAll(
            () -> assertNotNull(actualEmail),
            () -> assertEquals(Email.class, actualEmail.annotationType())
        );
    }

    @Test
    public void findBuiltInValidatorClass() {
        final Class<?> notEmptyStringValidator = BeanValidationHelper.findConstraintValidatorClass(NotEmpty.class, String.class);
        final List<String> notEmptyStringValidatorImpl = Arrays.asList(
            "org.hibernate.validator.internal.constraintvalidators.bv.notempty.NotEmptyValidatorForCharSequence",
            "org.apache.bval.constraints.NotEmptyValidatorForCharSequence"
        );

        assertTrue(() -> notEmptyStringValidatorImpl.contains(notEmptyStringValidator.getName()));

        final Class<?> notEmptyCollectionValidator = BeanValidationHelper.findConstraintValidatorClass(NotEmpty.class, List.class);
        final List<String> notEmptyCollectionValidatorImpl = Arrays.asList(
            "org.hibernate.validator.internal.constraintvalidators.bv.notempty.NotEmptyValidatorForCollection",
            "org.apache.bval.constraints.NotEmptyValidatorForCollection"
        );

        assertTrue(() -> notEmptyCollectionValidatorImpl.contains(notEmptyCollectionValidator.getName()));

        assertThrows(
            ConstraintValidatorException.class,
            () -> BeanValidationHelper.findConstraintValidatorClass(NotBlank.class, Integer.class)
        );
    }

    @Test
    public void findBuiltInValidatorClassByPrimitiveAndWrapperType() {
        final Class<?> minIntPrimitiveValidator = BeanValidationHelper.findConstraintValidatorClass(Min.class, Integer.class);
        final Class<?> minIntWrapperValidator = BeanValidationHelper.findConstraintValidatorClass(Min.class, int.class);

        assertEquals(minIntPrimitiveValidator, minIntWrapperValidator);

        final Class<?> assertTruePrimitiveValidator = BeanValidationHelper.findConstraintValidatorClass(AssertTrue.class, Boolean.class);
        final Class<?> assertTrueWrapperValidator = BeanValidationHelper.findConstraintValidatorClass(AssertTrue.class, boolean.class);

        assertEquals(assertTruePrimitiveValidator, assertTrueWrapperValidator);

        final Class<?> assertFalsePrimitiveValidator = BeanValidationHelper.findConstraintValidatorClass(AssertFalse.class, Boolean.class);
        final Class<?> assertFalseWrapperValidator = BeanValidationHelper.findConstraintValidatorClass(AssertFalse.class, boolean.class);

        assertEquals(assertFalsePrimitiveValidator, assertFalseWrapperValidator);
    }

    @Test
    public void findCustomValidatorClass() {
        final Class<?> fakeConstraintStringValidator = BeanValidationHelper.findConstraintValidatorClass(FakeConstraint.class, String.class);
        assertEquals(FakeConstraintStringValidator.class, fakeConstraintStringValidator);

        final Class<?> fakeConstraintStringBuilderValidator = BeanValidationHelper.findConstraintValidatorClass(FakeConstraint.class, StringBuilder.class);
        assertEquals(FakeConstraintCharSequenceValidator.class, fakeConstraintStringBuilderValidator);

        assertThrows(
            ConstraintValidatorException.class,
            () -> BeanValidationHelper.findConstraintValidatorClass(FakeConstraint.class, Number.class)
        );
    }

    @Test
    public void extractConditionalDescriptor() {
        final Set<ConditinalDescriptor> descriptors = BeanValidationHelper.getConditinalDescriptorOf(Address.class);

        assertEquals(1, descriptors.size());

        final ConditinalDescriptor descriptor = descriptors.iterator().next();

        assertAll(
            () -> assertEquals("street", descriptor.getName()),
            () -> assertEquals(Address.class, descriptor.getConstraintTarget().getDeclaringClass()),
            () -> assertEquals(String.class, descriptor.getConstraintTarget().getType()),
            () -> assertEquals(NotEmpty.class, descriptor.getActualConstraint().annotationType())
        );
    }

    @Test
    public void invokeConstraintValidator() {
        final Address address = new Address();
        final ConditinalDescriptor descriptor = BeanValidationHelper.getConditinalDescriptorOf(address.getClass()).iterator().next();
        final BooleanSupplier constraintValidatorExecutor = () -> BeanValidationHelper.invokeConstraintValidator(address, descriptor, null);

        assertFalse(constraintValidatorExecutor);

        address.setStreet("1234 Main Street");

        assertTrue(constraintValidatorExecutor);
    }

}

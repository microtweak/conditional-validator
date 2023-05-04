package com.github.microtweak.validator.conditional.tests;

import com.github.microtweak.validator.conditional.bean.Address;
import com.github.microtweak.validator.conditional.core.constraint.EmailWhen;
import com.github.microtweak.validator.conditional.core.constraint.NotNullWhen;
import com.github.microtweak.validator.conditional.core.exception.ConstraintValidatorException;
import com.github.microtweak.validator.conditional.core.internal.CvConstraintAnnotationDescriptor;
import com.github.microtweak.validator.conditional.core.internal.CvConstraintDescriptorImpl;
import com.github.microtweak.validator.conditional.core.internal.annotated.ValidationPoint;
import com.github.microtweak.validator.conditional.core.internal.helper.BeanValidationHelper;
import com.github.microtweak.validator.conditional.core.internal.helper.ConstraintValidatorRegistrar;
import com.github.microtweak.validator.conditional.core.spi.BeanValidationImplementationProvider;
import com.github.microtweak.validator.conditional.core.spi.PlatformProvider;
import com.github.microtweak.validator.conditional.internal.constraint.FakeConstraint;
import com.github.microtweak.validator.conditional.internal.constraint.FakeConstraintCharSequenceValidator;
import com.github.microtweak.validator.conditional.internal.constraint.FakeConstraintStringValidator;
import com.github.microtweak.validator.conditional.internal.literal.ConstraintLiteral;
import com.github.microtweak.validator.conditional.tags.ProviderTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintValidator;
import javax.validation.constraints.*;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.*;

@ProviderTest
public class BeanValidationHelperTests {

    private static final PlatformProvider platform = PlatformProvider.getInstance();
    private static final ConstraintValidatorRegistrar registrar = new ConstraintValidatorRegistrar();

    @BeforeAll
    public static void onInitTests() {
        final BeanValidationImplementationProvider bvProvider = BeanValidationImplementationProvider.getInstance();
        registrar.addValidators(bvProvider.getAvailableConstraintValidators());
    }

    @Test
    public void extractActualBeanValidationContraint() {
        final NotNullWhen notNullWhen = new ConstraintLiteral<>(NotNullWhen.class).build();
        final Annotation actualNotNull = new CvConstraintAnnotationDescriptor(notNullWhen).getActualBeanValidationConstraint();

        assertAll(
            () -> assertNotNull(actualNotNull),
            () -> assertEquals(NotNull.class, actualNotNull.annotationType())
        );

        final EmailWhen emailWhen = new ConstraintLiteral<>(EmailWhen.class)
            .attribute("regexp", ".*")
            .attribute("flags", new Pattern.Flag[0])
            .build();
        final Annotation actualEmail = new CvConstraintAnnotationDescriptor(emailWhen).getActualBeanValidationConstraint();

        assertAll(
            () -> assertNotNull(actualEmail),
            () -> assertEquals(Email.class, actualEmail.annotationType())
        );
    }

    @Test
    public void findBuiltInValidatorClass() {
        final Class<?> notEmptyStringValidator = BeanValidationHelper.findConstraintValidatorClass(registrar, NotEmpty.class, String.class);
        final List<String> notEmptyStringValidatorImpl = Arrays.asList(
            "org.hibernate.validator.internal.constraintvalidators.bv.notempty.NotEmptyValidatorForCharSequence",
            "org.apache.bval.constraints.NotEmptyValidatorForCharSequence"
        );

        assertTrue(() -> notEmptyStringValidatorImpl.contains(notEmptyStringValidator.getName()));

        final Class<?> notEmptyCollectionValidator = BeanValidationHelper.findConstraintValidatorClass(registrar, NotEmpty.class, List.class);
        final List<String> notEmptyCollectionValidatorImpl = Arrays.asList(
            "org.hibernate.validator.internal.constraintvalidators.bv.notempty.NotEmptyValidatorForCollection",
            "org.apache.bval.constraints.NotEmptyValidatorForCollection"
        );

        assertTrue(() -> notEmptyCollectionValidatorImpl.contains(notEmptyCollectionValidator.getName()));

        assertThrows(
            ConstraintValidatorException.class,
            () -> BeanValidationHelper.findConstraintValidatorClass(registrar, NotBlank.class, Integer.class)
        );
    }

    @Test
    public void findBuiltInValidatorClassByPrimitiveAndWrapperType() {
        final Class<?> minIntPrimitiveValidator = BeanValidationHelper.findConstraintValidatorClass(registrar, Min.class, Integer.class);
        final Class<?> minIntWrapperValidator = BeanValidationHelper.findConstraintValidatorClass(registrar, Min.class, int.class);

        assertEquals(minIntPrimitiveValidator, minIntWrapperValidator);

        final Class<?> assertTruePrimitiveValidator = BeanValidationHelper.findConstraintValidatorClass(registrar, AssertTrue.class, Boolean.class);
        final Class<?> assertTrueWrapperValidator = BeanValidationHelper.findConstraintValidatorClass(registrar, AssertTrue.class, boolean.class);

        assertEquals(assertTruePrimitiveValidator, assertTrueWrapperValidator);

        final Class<?> assertFalsePrimitiveValidator = BeanValidationHelper.findConstraintValidatorClass(registrar, AssertFalse.class, Boolean.class);
        final Class<?> assertFalseWrapperValidator = BeanValidationHelper.findConstraintValidatorClass(registrar, AssertFalse.class, boolean.class);

        assertEquals(assertFalsePrimitiveValidator, assertFalseWrapperValidator);
    }

    @Test
    public void findCustomValidatorClass() {
        final Class<?> fakeConstraintStringValidator = BeanValidationHelper.findConstraintValidatorClass(registrar, FakeConstraint.class, String.class);
        assertEquals(FakeConstraintStringValidator.class, fakeConstraintStringValidator);

        final Class<?> fakeConstraintStringBuilderValidator = BeanValidationHelper.findConstraintValidatorClass(registrar, FakeConstraint.class, StringBuilder.class);
        assertEquals(FakeConstraintCharSequenceValidator.class, fakeConstraintStringBuilderValidator);

        assertThrows(
            ConstraintValidatorException.class,
            () -> BeanValidationHelper.findConstraintValidatorClass(registrar, FakeConstraint.class, Number.class)
        );
    }

    @Test
    public void extractValidationPointAndConstraintDescriptor() {
        for (final ValidationPoint validationPoint : BeanValidationHelper.getAllValidationPointsAt(Address.class)) {
            final CvConstraintDescriptorImpl<?> descriptor = BeanValidationHelper.getAllConstraintDescriptorOf(validationPoint, registrar).iterator().next();

            assertAll(
                () -> assertEquals("street", validationPoint.getName()),
                () -> assertEquals(Address.class, validationPoint.getDeclaringClass()),
                () -> assertEquals(String.class, validationPoint.getType()),
                () -> assertEquals(NotEmpty.class, descriptor.getAnnotation().annotationType())
            );
        }
    }

    @Test
    public void invokeConstraintValidator() {
        final Address address = new Address();

        for (final ValidationPoint validationPoint : BeanValidationHelper.getAllValidationPointsAt(address.getClass())) {
            final ConstraintValidator<Annotation, Object> validator = BeanValidationHelper.getAllConstraintDescriptorOf(validationPoint, registrar).stream()
                .map(descriptor -> BeanValidationHelper.getInitializedConstraintValidator(platform.getConstraintValidatorFactory(), descriptor))
                .findFirst()
                .orElseThrow(() -> new NullPointerException("No validator found"));

            final BooleanSupplier constraintValidatorExecutor = () -> validator.isValid(validationPoint.getValidatedValue(address), null);

            assertFalse(constraintValidatorExecutor);

            address.setStreet("1234 Main Street");

            assertTrue(constraintValidatorExecutor);
        }
    }

}

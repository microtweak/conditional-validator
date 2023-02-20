package com.github.microtweak.validator.conditional.internal;

import com.github.microtweak.validator.conditional.core.constraint.AssertTrueWhen;
import com.github.microtweak.validator.conditional.core.constraint.DecimalMaxWhen;
import com.github.microtweak.validator.conditional.core.constraint.SizeWhen;
import com.github.microtweak.validator.conditional.core.internal.CvConstraintAnnotationDescriptor;
import com.github.microtweak.validator.conditional.core.internal.CvConstraintDescriptorImpl;
import com.github.microtweak.validator.conditional.core.internal.CvMessageInterpolatorContext;
import com.github.microtweak.validator.conditional.core.spi.PlatformProvider;
import com.github.microtweak.validator.conditional.internal.literal.ConstraintLiteral;
import com.github.microtweak.validator.conditional.junit5.ProviderTest;
import org.apache.commons.lang3.function.TriFunction;
import org.junit.jupiter.api.Test;

import javax.validation.MessageInterpolator;
import javax.validation.metadata.ConstraintDescriptor;
import java.lang.annotation.Annotation;
import java.util.Locale;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ProviderTest
public class CvMessageInterpolatorContextTests {

    private static final PlatformProvider platform = PlatformProvider.getInstance();

    private static final TriFunction<Annotation, Object, Locale, String> interpolateMessageCustomLocaleFn = (conditionalConstraint, validatedValue, customLocale) -> {
        final CvConstraintAnnotationDescriptor annotationDescriptor = new CvConstraintAnnotationDescriptor(conditionalConstraint);

        final ConstraintDescriptor<?> constraintDescriptor = new CvConstraintDescriptorImpl<>(annotationDescriptor, null);

        final MessageInterpolator.Context interpolatorContext = new CvMessageInterpolatorContext(constraintDescriptor, validatedValue);

        return platform.getMessageInterpolator().interpolate(constraintDescriptor.getMessageTemplate(), interpolatorContext, customLocale);
    };

    private static final BiFunction<Annotation, Object, String> interpolateMessageFn = (conditionalConstraint, validatedValue) ->
            interpolateMessageCustomLocaleFn.apply(conditionalConstraint, validatedValue, Locale.ENGLISH);

    @Test
    public void interpolateSimpleMessageInline() {
        final AssertTrueWhen assertTrueWhen = new ConstraintLiteral<>(AssertTrueWhen.class)
            .message("must be true")
            .build();

        final String interpolated = interpolateMessageFn.apply(assertTrueWhen, null);

        assertEquals("must be true", interpolated);
    }


    @Test
    public void interpolateSimpleMessageResourceBundle() {
        final AssertTrueWhen assertTrueWhen = new ConstraintLiteral<>(AssertTrueWhen.class).build();

        final String interpolated = interpolateMessageFn.apply(assertTrueWhen, null);

        assertEquals("must be true", interpolated);
    }

    @Test
    public void interpolateWithCustomLocaleSimpleMessageResourceBundle() {
        final AssertTrueWhen assertTrueWhen = new ConstraintLiteral<>(AssertTrueWhen.class).build();

        final String interpolated = interpolateMessageCustomLocaleFn.apply(assertTrueWhen, false, Locale.ITALIAN);

        assertEquals("deve essere true", interpolated);
    }

    @Test
    public void interpolateMessageWithAttributesInline() {
        final SizeWhen sizeWhen = new ConstraintLiteral<>(SizeWhen.class)
            .message("size must be between {min} and {max}")
            .attribute("min", 5)
            .attribute("max", 10)
            .build();

        final String interpolated = interpolateMessageFn.apply(sizeWhen, "");

        assertEquals("size must be between 5 and 10", interpolated);
    }

    @Test
    public void interpolateMessageWithAttributesResourceBundle() {
        final SizeWhen sizeWhen = new ConstraintLiteral<>(SizeWhen.class)
            .attribute("min", 5)
            .attribute("max", 10)
            .build();

        final String interpolated = interpolateMessageFn.apply(sizeWhen, "");

        assertEquals("size must be between 5 and 10", interpolated);
    }

    @Test
    public void interpolateMessageWithExpressionLanguageInline() {
        final String defaultMessage = "must be less than ${inclusive == true ? 'or equal to ' : ''}{value}";
        final String expectedValue = "10.0";
        final String validatedValue = "1.0";

        assertAll(
            () -> {
                final DecimalMaxWhen decimalMaxWhen = new ConstraintLiteral<>(DecimalMaxWhen.class)
                    .message(defaultMessage)
                    .attribute("value", "10.0")
                    .attribute("inclusive", false)
                    .build();

                final String interpolated = interpolateMessageFn.apply(decimalMaxWhen, validatedValue);
                assertEquals("must be less than " + expectedValue, interpolated);
            },
            () -> {
                final DecimalMaxWhen decimalMaxWhen = new ConstraintLiteral<>(DecimalMaxWhen.class)
                    .message(defaultMessage)
                    .attribute("value", "10.0")
                    .attribute("inclusive", true)
                    .build();

                final String interpolated = interpolateMessageFn.apply(decimalMaxWhen, validatedValue);
                assertEquals("must be less than or equal to " + expectedValue, interpolated);
            }
        );
    }

    @Test
    public void interpolateMessageWithExpressionLanguageResourceBundle() {
        final String expectedValue = "10.0";
        final String validatedValue = "1.0";

        assertAll(
            () -> {
                final DecimalMaxWhen decimalMaxWhen = new ConstraintLiteral<>(DecimalMaxWhen.class)
                    .attribute("value", "10.0")
                    .attribute("inclusive", false)
                    .build();

                final String interpolated = interpolateMessageFn.apply(decimalMaxWhen, validatedValue);
                assertEquals("must be less than " + expectedValue, interpolated);
            },
            () -> {
                final DecimalMaxWhen decimalMaxWhen = new ConstraintLiteral<>(DecimalMaxWhen.class)
                    .attribute("value", "10.0")
                    .attribute("inclusive", true)
                    .build();

                final String interpolated = interpolateMessageFn.apply(decimalMaxWhen, validatedValue);
                assertEquals("must be less than or equal to " + expectedValue, interpolated);
            }
        );
    }

}

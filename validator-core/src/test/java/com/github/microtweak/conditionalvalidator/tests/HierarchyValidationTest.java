package com.github.microtweak.conditionalvalidator.tests;

import com.github.microtweak.conditionalvalidator.tags.ProviderTest;
import com.github.microtweak.conditionalvalidator.bean.Book;
import com.github.microtweak.conditionalvalidator.bean.Product;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ProviderTest
public class HierarchyValidationTest extends BaseBvIntegrationTest {

    @Test
    public void validationWithoutInheritance() {
        Product product = new Product();
        assertTrue( applyValidation(product).isEmpty() );

        product.setApplyValidation(true);
        Set<ConstraintViolation<Product>> violations = applyValidation(product);

        assertHasPropertyViolation(violations, "description");
    }

    @Test
    public void validationWithInheritedConstraints() {
        Book book = new Book();
        assertTrue( applyValidation(book).isEmpty() );

        book.setApplyValidation(true);
        Set<ConstraintViolation<Product>> violations = applyValidation(book);

        assertAll(
            () -> assertHasPropertyViolation(violations, "description"),
            () -> assertHasPropertyViolation(violations, "isbn")
        );
    }

}
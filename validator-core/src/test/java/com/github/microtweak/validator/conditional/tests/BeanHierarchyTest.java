package com.github.microtweak.validator.conditional.tests;

import com.github.microtweak.validator.conditional.bean.Book;
import com.github.microtweak.validator.conditional.bean.Product;
import com.github.microtweak.validator.conditional.junit5.ProviderTest;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ProviderTest
public class BeanHierarchyTest extends BaseConstraintViolationTest {

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
package com.libraryapi.validators;

import com.libraryapi.model.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;


@RunWith(MockitoJUnitRunner.class)
public class DefaultCommonValidatorTest {

    @InjectMocks
    private DefaultCommonValidator validator;

    @Test
    public void validateBookRequest_withInconsistentBook_returnValidationError() {
        List<Book> existingBooksWithIsbn = List.of(new Book(UUID.randomUUID(), "000001", "John Wick", "Adam Smith", false));
        List<FieldError> validationErrors = new ArrayList<>();

        validationErrors = validator.validateBook(existingBooksWithIsbn, validationErrors, "John Wick", "Paul", "000001");

        assertThat(validationErrors, hasSize(1));
        assertThat(
                validationErrors.get(0).getObjectName(),
                equalTo("BookCommand")
        );
        assertThat(validationErrors.get(0).getField(), equalTo("isbn"));
        assertThat(validationErrors.get(0).getRejectedValue(), equalTo("000001"));
        assertThat(validationErrors.get(0).getCode(), equalTo("book.is.not.valid"));
        assertThat(validationErrors.get(0).getDefaultMessage(), equalTo("Books with the same ISBN must have the same title and author"));
    }

    @Test
    public void validateBookRequest_withConsistentBook_returnEmptyValidationErrors() {
        List<Book> existingBooksWithIsbn = List.of(new Book(UUID.randomUUID(), "000001", "John Wick", "Adam Smith", false));
        List<FieldError> validationErrors = new ArrayList<>();

        validationErrors = validator.validateBook(existingBooksWithIsbn, validationErrors, "John Wick", "Adam Smith", "000001");

        assertThat(validationErrors, hasSize(0));

    }


}

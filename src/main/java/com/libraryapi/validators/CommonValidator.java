package com.libraryapi.validators;

import com.libraryapi.model.Book;
import org.springframework.validation.FieldError;

import java.util.List;

public interface CommonValidator {

    List<FieldError> validateBook(List<Book> existingBooksWithIsbn, List<FieldError> validationErrors, String title,
                                  String author, String isbn);

}

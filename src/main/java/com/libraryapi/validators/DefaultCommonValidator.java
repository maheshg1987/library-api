package com.libraryapi.validators;

import com.libraryapi.model.Book;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.List;

@Component
public class DefaultCommonValidator implements CommonValidator {


    @Override
    public List<FieldError> validateBook(List<Book> existingBooksWithIsbn, List<FieldError> validationErrors,
                                         String title, String author, String isbn) {
        boolean inconsistentBook = existingBooksWithIsbn.stream()
                .anyMatch(existingBook ->
                        !existingBook.getTitle().equals(title) ||
                                !existingBook.getAuthor().equals(author));

        if (inconsistentBook) {
            validationErrors.add(new FieldError(
                    "BookCommand",
                    "isbn",
                    isbn,
                    false,
                    new String[]{"book.is.not.valid"},
                    null,
                    "Books with the same ISBN must have the same title and author"
            ));

        }
        return validationErrors;
    }
}

package com.libraryapi.commandhandlers;


import an.awesome.pipelinr.Command;
import com.libraryapi.commands.BookCommand;
import com.libraryapi.dao.repositories.BookRepository;
import com.libraryapi.dtos.BookResponseDTO;
import com.libraryapi.exception.ValidationException;
import com.libraryapi.mappers.CommonMapper;
import com.libraryapi.model.Book;
import com.libraryapi.validators.CommonValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookCommandHandler implements Command.Handler<BookCommand, BookResponseDTO> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final BookRepository bookRepository;
    private final CommonMapper mapper;
    private final CommonValidator validator;

    public BookCommandHandler(BookRepository bookRepository, CommonMapper mapper, CommonValidator validator) {
        this.bookRepository = bookRepository;
        this.mapper = mapper;
        this.validator = validator;
    }


    @Override
    public BookResponseDTO handle(BookCommand command) {
        List<FieldError> validationErrors = new ArrayList<>();
        logger.debug("Finding existing Books requests to process for isbn: {}", command.getIsbn());
        List<Book> existingBooksWithIsbn = bookRepository.findByIsbn(command.getIsbn());
        logger.debug("ExistingBooksWithIsbn: {}", existingBooksWithIsbn.size());
        if (!existingBooksWithIsbn.isEmpty()) {
            validationErrors = validator.validateBook(existingBooksWithIsbn, validationErrors, command.getTitle(),
                    command.getAuthor(), command.getIsbn());

            if (!validationErrors.isEmpty()) {
                logger.debug("Validation failed for book request for isbn: {}", command.getIsbn());
                throw new ValidationException(validationErrors);
            }
        }

        Book savedBook = bookRepository.saveAndFlush(createBook(command));
        logger.debug("Book saved successfully with the id: {}", savedBook.getId());
        return mapper.toBookResponseDTO(savedBook);
    }


    private Book createBook(BookCommand command) {
        return new Book(
                command.getIsbn(),
                command.getTitle(),
                command.getAuthor(),
                command.isBorrowed()
        );
    }

}

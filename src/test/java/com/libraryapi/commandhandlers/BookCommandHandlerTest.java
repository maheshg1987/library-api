package com.libraryapi.commandhandlers;

import com.libraryapi.commands.BookCommand;
import com.libraryapi.dao.repositories.BookRepository;
import com.libraryapi.dtos.BookResponseDTO;
import com.libraryapi.exception.ValidationException;
import com.libraryapi.mappers.CommonMapper;
import com.libraryapi.model.Book;
import com.libraryapi.validators.DefaultCommonValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookCommandHandlerTest {

    @Mock
    private BookRepository bookRepository;

    @Spy
    private DefaultCommonValidator validator;

    @Spy
    private CommonMapper mapper;

    @InjectMocks
    private BookCommandHandler commandHandler;

    private BookCommand command;

    @Before
    public void init() {
        command = new BookCommand();
        command.setAuthor("Adam Smith");
        command.setBorrowed(false);
        command.setIsbn("000001");
        command.setTitle("John Wick");
    }

    @Test
    public void handle_success_returnsBookResponseDTO() {
        List<Book> existingBooks = Collections.emptyList();
        when(bookRepository.findByIsbn(command.getIsbn())).thenReturn(existingBooks);

        Book savedBook = new Book(command.getIsbn(), command.getTitle(), command.getAuthor(), false);
        when(bookRepository.saveAndFlush(any(Book.class))).thenReturn(savedBook);

        BookResponseDTO responseDTO = new BookResponseDTO(UUID.randomUUID(), "000001", "John Wick", "Adam Smith", false);
        when(mapper.toBookResponseDTO(savedBook)).thenReturn(responseDTO);


        BookResponseDTO result = commandHandler.handle(command);

        assertThat(result.getIsbn(), equalTo(command.getIsbn()));
        assertThat(result.getAuthor(), equalTo(command.getAuthor()));
        assertThat(result.getId(), notNullValue());
        assertThat(result.getTitle(), equalTo(command.getTitle()));
        assertThat(result.isBorrowed(), equalTo(command.isBorrowed()));


        verify(bookRepository).findByIsbn(command.getIsbn());
        verifyNoInteractions(validator);
        verify(bookRepository).saveAndFlush(any(Book.class));
        verify(mapper).toBookResponseDTO(savedBook);
        verifyNoMoreInteractions(bookRepository, validator, mapper);
    }

    @Test
    public void handle_sameIsbnAndSameAuthorWithDifferentTitle_inconsistentBookValidation_throwsValidationException() {

        List<Book> existingBooks = List.of(new Book("000001", "Jon Wick 2", "Adam Smith", false));

        when(bookRepository.findByIsbn(command.getIsbn())).thenReturn(existingBooks);
        try {
            commandHandler.handle(command);
            fail("Expected ValidationException");
        } catch (ValidationException e) {
            assertThat(e.getFieldErrors(), hasSize(1));
            FieldError fieldError = e.getFieldErrors().get(0);
            assertThat(
                    fieldError.getObjectName(),
                    equalTo("BookCommand")
            );
            assertThat(fieldError.getField(), equalTo("isbn"));
            assertThat(fieldError.getRejectedValue(), equalTo("000001"));
            assertThat(fieldError.getCode(), equalTo("book.is.not.valid"));
            assertThat(
                    fieldError.getDefaultMessage(),
                    equalTo("Books with the same ISBN must have the same title and author")
            );
        }
        verify(bookRepository).findByIsbn(anyString());
        verify(validator).validateBook(anyList(), anyList(), anyString(), anyString(), anyString());
        verifyNoMoreInteractions(bookRepository, validator);
    }

    @Test
    public void handle_sameIsbnAndSameTitleWithDifferentAuthor_inconsistentBookValidation_throwsValidationException() {

        List<Book> existingBooks = List.of(new Book("000001", "Jon Wick", "Paul Smith", false));

        when(bookRepository.findByIsbn(command.getIsbn())).thenReturn(existingBooks);
        try {
            commandHandler.handle(command);
            fail("Expected ValidationException");
        } catch (ValidationException e) {
            assertThat(e.getFieldErrors(), hasSize(1));
            FieldError fieldError = e.getFieldErrors().get(0);
            assertThat(
                    fieldError.getObjectName(),
                    equalTo("BookCommand")
            );
            assertThat(fieldError.getField(), equalTo("isbn"));
            assertThat(fieldError.getRejectedValue(), equalTo("000001"));
            assertThat(fieldError.getCode(), equalTo("book.is.not.valid"));
            assertThat(
                    fieldError.getDefaultMessage(),
                    equalTo("Books with the same ISBN must have the same title and author")
            );
        }
        verify(bookRepository).findByIsbn(anyString());
        verify(validator).validateBook(anyList(), anyList(), anyString(), anyString(), anyString());
        verifyNoMoreInteractions(bookRepository, validator);
    }

    @Test
    public void handle_differentIsbnWithSameAuthorAndTitle_success_returnsBookResponseDTO() {

        List<Book> existingBooks = List.of(new Book("000002", "John Wick", "Adam Smith", false));

        when(bookRepository.findByIsbn(command.getIsbn())).thenReturn(existingBooks);

        Book savedBook = new Book(command.getIsbn(), command.getTitle(), command.getAuthor(), false);
        when(bookRepository.saveAndFlush(any(Book.class))).thenReturn(savedBook);

        BookResponseDTO responseDTO = new BookResponseDTO(UUID.randomUUID(), "000001", "John Wick", "Adam Smith", false);
        when(mapper.toBookResponseDTO(savedBook)).thenReturn(responseDTO);


        BookResponseDTO result = commandHandler.handle(command);

        assertThat(result.getIsbn(), equalTo(command.getIsbn()));
        assertThat(result.getAuthor(), equalTo(command.getAuthor()));
        assertThat(result.getId(), notNullValue());
        assertThat(result.getTitle(), equalTo(command.getTitle()));
        assertThat(result.isBorrowed(), equalTo(command.isBorrowed()));


        verify(bookRepository).findByIsbn(command.getIsbn());
        verify(validator).validateBook(anyList(), anyList(), anyString(), anyString(), anyString());
        verify(bookRepository).saveAndFlush(any(Book.class));
        verify(mapper).toBookResponseDTO(savedBook);
        verifyNoMoreInteractions(bookRepository, validator, mapper);
    }
}
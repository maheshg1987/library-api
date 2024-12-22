package com.libraryapi.commandhandlers;

import com.libraryapi.commands.ReturnBookCommand;
import com.libraryapi.dao.repositories.BookRepository;
import com.libraryapi.dao.repositories.BorrowerRepository;
import com.libraryapi.model.Book;
import com.libraryapi.model.Borrower;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReturnBookCommandHandlerTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BorrowerRepository borrowerRepository;

    @InjectMocks
    private ReturnBookCommandHandler commandHandler;

    private ReturnBookCommand command;

    @Before
    public void init() {
        command = new ReturnBookCommand();
        command.setBorrowerId(UUID.randomUUID());
        command.setBookId(UUID.randomUUID());
    }

    @Test
    public void handle_borrowerNotFound_returnsNotFound() {
        when(borrowerRepository.findById(command.getBorrowerId())).thenReturn(Optional.empty());

        ResponseEntity<String> response = commandHandler.handle(command);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        assertThat(response.getBody(), equalTo("Borrower not found"));

        verify(borrowerRepository).findById(command.getBorrowerId());
        verifyNoMoreInteractions(borrowerRepository);
        verifyNoInteractions(bookRepository);
    }

    @Test
    public void handle_bookNotFound_returnsNotFound() {
        Borrower savedBorrower = new Borrower("Test User", "test@test.com");
        when(borrowerRepository.findById(command.getBorrowerId())).thenReturn(Optional.of(savedBorrower));
        when(bookRepository.findById(command.getBookId())).thenReturn(Optional.empty());

        ResponseEntity<String> response = commandHandler.handle(command);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        assertThat(response.getBody(), equalTo("Book not found"));

        verify(borrowerRepository).findById(command.getBorrowerId());
        verify(bookRepository).findById(command.getBookId());
        verifyNoMoreInteractions(borrowerRepository, bookRepository);
    }

    @Test
    public void handle_bookAlreadyNotBorrowed_returnsConflict() {
        Borrower savedBorrower = new Borrower("Test User", "test@test.com");
        Book book = new Book(UUID.randomUUID(), "000001", "John Wick", "Adam Smith", false);

        when(borrowerRepository.findById(command.getBorrowerId())).thenReturn(Optional.of(savedBorrower));
        when(bookRepository.findById(command.getBookId())).thenReturn(Optional.of(book));

        ResponseEntity<String> response = commandHandler.handle(command);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CONFLICT));
        assertThat(response.getBody(), equalTo("Book is not currently borrowed"));

        verify(borrowerRepository).findById(command.getBorrowerId());
        verify(bookRepository).findById(command.getBookId());
        verifyNoMoreInteractions(borrowerRepository, bookRepository);

    }

    @Test
    public void handle_success_returnsBookSuccessfully() {

        Borrower savedBorrower = new Borrower("Test User", "test@test.com");
        Book book = new Book(UUID.randomUUID(), "000001", "John Wick", "Adam Smith", true);

        when(borrowerRepository.findById(command.getBorrowerId())).thenReturn(Optional.of(savedBorrower));
        when(bookRepository.findById(command.getBookId())).thenReturn(Optional.of(book));

        ResponseEntity<String> response = commandHandler.handle(command);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo("Book returned successfully"));
        assertThat(book.isBorrowed(), is(false));

        verify(borrowerRepository).findById(command.getBorrowerId());
        verify(bookRepository).findById(command.getBookId());
        verify(bookRepository).save(book);
        verifyNoMoreInteractions(borrowerRepository, bookRepository);

    }

}

package com.libraryapi.controllers;

import an.awesome.pipelinr.Pipeline;
import com.libraryapi.commands.BookCommand;
import com.libraryapi.dao.repositories.BookRepository;
import com.libraryapi.dtos.BookResponseDTO;
import com.libraryapi.model.Book;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookControllerTest {

    @InjectMocks
    private BookController bookController;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private Pipeline pipeline;

    @Mock
    private HttpServletResponse httpResponse;

    private BookCommand command;
    private BookResponseDTO bookResponseDTO;
    private Book book;

    @Before
    public void init() {
        command = new BookCommand();
        command.setIsbn("00001");
        command.setTitle("John Wick");
        command.setAuthor("Adam Smith");
        command.setBorrowed(false);

        bookResponseDTO = new BookResponseDTO();
        bookResponseDTO.setIsbn("00001");
        bookResponseDTO.setTitle("John Wick");
        bookResponseDTO.setAuthor("Adam Smith");
        bookResponseDTO.setBorrowed(false);

        book = new Book("00001", "John Wick", "Adam Smith", false);
    }

    @Test
    public void registerBook_success() {

        when(pipeline.send(command)).thenReturn(bookResponseDTO);


        ResponseEntity<BookResponseDTO> response = bookController.registerBook(command, httpResponse);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertThat(response.getBody(), equalTo(bookResponseDTO));
        verify(pipeline, times(1)).send(command);
        verifyNoMoreInteractions(pipeline);
    }

    @Test
    public void getAllBooks_success() {
        List<Book> books = List.of(book);
        when(bookRepository.findAll()).thenReturn(books);

        ResponseEntity<List<Book>> response = bookController.getAllBooks();

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(books));
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void getAllBooks_empty() {
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Book>> response = bookController.getAllBooks();

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertTrue(response.getBody().isEmpty());
        verify(bookRepository, times(1)).findAll();
    }

}

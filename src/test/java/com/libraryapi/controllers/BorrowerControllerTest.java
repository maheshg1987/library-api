package com.libraryapi.controllers;

import an.awesome.pipelinr.Pipeline;
import com.libraryapi.commands.BorrowerBookCommand;
import com.libraryapi.commands.BorrowerCommand;
import com.libraryapi.commands.ReturnBookCommand;
import com.libraryapi.dao.repositories.BorrowerRepository;
import com.libraryapi.dtos.BorrowerResponseDTO;
import com.libraryapi.model.Borrower;
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
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BorrowerControllerTest {

    @InjectMocks
    private BorrowerController borrowerController;

    @Mock
    private BorrowerRepository borrowerRepository;

    @Mock
    private Pipeline pipeline;

    @Mock
    private HttpServletResponse httpResponse;

    private BorrowerCommand borrowerCommand;
    private BorrowerBookCommand borrowerBookCommand;
    private ReturnBookCommand returnBookCommand;
    private BorrowerResponseDTO borrowerResponseDTO;
    private Borrower borrower;

    @Before
    public void init() {

        borrowerCommand = new BorrowerCommand();
        borrowerCommand.setEmail("Test@test.com");
        borrowerCommand.setName("Test User");

        borrowerBookCommand = new BorrowerBookCommand();
        borrowerBookCommand.setBookId(UUID.randomUUID());
        borrowerBookCommand.setBorrowerId(UUID.randomUUID());

        borrowerResponseDTO = new BorrowerResponseDTO();
        borrowerResponseDTO.setEmail("Test@test.com");
        borrowerResponseDTO.setId(UUID.randomUUID());
        borrowerResponseDTO.setName("Test User");

        returnBookCommand = new ReturnBookCommand();
        returnBookCommand.setBookId(UUID.randomUUID());
        returnBookCommand.setBorrowerId(UUID.randomUUID());

        borrower = new Borrower(UUID.randomUUID(), "Test User", "Test@test.com");
    }

    @Test
    public void registerBorrower_success() {

        when(pipeline.send(borrowerCommand)).thenReturn(borrowerResponseDTO);


        ResponseEntity<BorrowerResponseDTO> response = borrowerController.registerBorrower(borrowerCommand, httpResponse);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertThat(response.getBody(), equalTo(borrowerResponseDTO));
        verify(pipeline, times(1)).send(borrowerCommand);
        verifyNoMoreInteractions(pipeline);
    }

    @Test
    public void borrowBook_success() {

        when(pipeline.send(borrowerBookCommand)).
                thenReturn(ResponseEntity.status(HttpStatus.OK).body("Book borrowed successfully"));


        ResponseEntity<String> response = borrowerController.borrowBook(borrowerBookCommand);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo("Book borrowed successfully"));
        verify(pipeline, times(1)).send(borrowerBookCommand);
        verifyNoMoreInteractions(pipeline);
    }

    @Test
    public void returnBook_success() {

        when(pipeline.send(returnBookCommand)).
                thenReturn(ResponseEntity.status(HttpStatus.OK).body("Book returned successfully"));


        ResponseEntity<String> response = borrowerController.returnBook(returnBookCommand);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo("Book returned successfully"));
        verify(pipeline, times(1)).send(returnBookCommand);
        verifyNoMoreInteractions(pipeline);
    }

    @Test
    public void getAllBorrowers_success() {
        List<Borrower> borrowers = List.of(borrower);
        when(borrowerRepository.findAll()).thenReturn(borrowers);

        ResponseEntity<List<Borrower>> response = borrowerController.getAllBorrower();

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(borrowers));
        verify(borrowerRepository, times(1)).findAll();
    }

    @Test
    public void getAllBorrowers_empty() {
        when(borrowerRepository.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Borrower>> response = borrowerController.getAllBorrower();

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertTrue(response.getBody().isEmpty());
        verify(borrowerRepository, times(1)).findAll();
    }


}

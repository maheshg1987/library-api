package com.libraryapi.commandhandlers;

import com.libraryapi.commands.BorrowerCommand;
import com.libraryapi.dao.repositories.BorrowerRepository;
import com.libraryapi.dtos.BorrowerResponseDTO;
import com.libraryapi.mappers.CommonMapper;
import com.libraryapi.model.Borrower;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class BorrowerCommandHandlerTest {

    @Mock
    private BorrowerRepository borrowerRepository;

    @Spy
    private CommonMapper mapper;

    @InjectMocks
    private BorrowerCommandHandler commandHandler;

    private BorrowerCommand command;

    @Before
    public void init() {
        command = new BorrowerCommand();
        command.setEmail("test@test.com");
        command.setName("test user");
    }

    @Test
    public void handle_success() {

        Borrower savedBorrower = new Borrower(command.getName(), command.getEmail());
        BorrowerResponseDTO responseDTO = new BorrowerResponseDTO();
        responseDTO.setName(savedBorrower.getName());
        responseDTO.setEmail(savedBorrower.getEmail());

        when(borrowerRepository.saveAndFlush(any(Borrower.class))).thenReturn(savedBorrower);
        when(mapper.toBorrowerResponseDTO(savedBorrower)).thenReturn(responseDTO);

        BorrowerResponseDTO borrowerResponseDTO = commandHandler.handle(command);

        assertThat(borrowerResponseDTO.getName(), equalTo(command.getName()));
        assertThat(borrowerResponseDTO.getEmail(), equalTo(command.getEmail()));

        verify(borrowerRepository).saveAndFlush(any());
        verifyNoMoreInteractions(borrowerRepository);
        verify(mapper).toBorrowerResponseDTO(any());
        verifyNoMoreInteractions(mapper);
    }

}

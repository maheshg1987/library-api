package com.libraryapi.mappers;

import com.libraryapi.dtos.BookResponseDTO;
import com.libraryapi.dtos.BorrowerResponseDTO;
import com.libraryapi.model.Book;
import com.libraryapi.model.Borrower;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCommonMapperTest {

    @InjectMocks
    private DefaultCommonMapper mapper;

    @Test
    public void toBookResponseDto_returnBookResponseDto() {

        Book book = new Book(UUID.randomUUID(), "000001", "John Wick", "Adam Smith", false);

        BookResponseDTO bookResponseDTO = mapper.toBookResponseDTO(book);

        assertThat(book.getAuthor(), equalTo(bookResponseDTO.getAuthor()));
        assertThat(book.getId(), equalTo(bookResponseDTO.getId()));
        assertThat(book.getIsbn(), equalTo(bookResponseDTO.getIsbn()));
        assertThat(book.getTitle(), equalTo(bookResponseDTO.getTitle()));
        assertThat(book.isBorrowed(), equalTo(bookResponseDTO.isBorrowed()));

    }


    @Test
    public void toBorrowerResponseDTO_returnBorrowerResponseDto() {

        Borrower borrower = new Borrower(UUID.randomUUID(), "Mahesh Guruge", "test@test.com");

        BorrowerResponseDTO borrowerResponseDTO = mapper.toBorrowerResponseDTO(borrower);

        assertThat(borrower.getEmail(), equalTo(borrowerResponseDTO.getEmail()));
        assertThat(borrower.getId(), equalTo(borrowerResponseDTO.getId()));
        assertThat(borrower.getName(), equalTo(borrowerResponseDTO.getName()));

    }


}

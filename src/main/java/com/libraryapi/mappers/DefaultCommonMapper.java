package com.libraryapi.mappers;

import com.libraryapi.dtos.BookResponseDTO;
import com.libraryapi.dtos.BorrowerResponseDTO;
import com.libraryapi.model.Book;
import com.libraryapi.model.Borrower;
import org.springframework.stereotype.Component;

@Component
public class DefaultCommonMapper implements CommonMapper {

    @Override
    public BookResponseDTO toBookResponseDTO(Book book) {
        return new BookResponseDTO(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.isBorrowed()
        );
    }

    @Override
    public BorrowerResponseDTO toBorrowerResponseDTO(Borrower borrower) {
        return new BorrowerResponseDTO(
                borrower.getId(),
                borrower.getName(),
                borrower.getEmail()
        );
    }

}

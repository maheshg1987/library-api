package com.libraryapi.mappers;

import com.libraryapi.dtos.BookResponseDTO;
import com.libraryapi.dtos.BorrowerResponseDTO;
import com.libraryapi.model.Book;
import com.libraryapi.model.Borrower;

public interface CommonMapper {

    BookResponseDTO toBookResponseDTO(Book book);

    BorrowerResponseDTO toBorrowerResponseDTO(Borrower borrower);

}

package com.libraryapi.commands;

import an.awesome.pipelinr.Command;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class ReturnBookCommand implements Command<ResponseEntity<String>> {

    @NotNull(message = "Borrower Id is mandatory")
    private UUID borrowerId;

    @NotNull(message = "Book Id is mandatory")
    private UUID bookId;

    public UUID getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(UUID borrowerId) {
        this.borrowerId = borrowerId;
    }

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }


}

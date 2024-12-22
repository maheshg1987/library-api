package com.libraryapi.commandhandlers;


import an.awesome.pipelinr.Command;
import com.libraryapi.commands.BorrowerBookCommand;
import com.libraryapi.dao.repositories.BookRepository;
import com.libraryapi.dao.repositories.BorrowerRepository;
import com.libraryapi.model.Book;
import com.libraryapi.model.Borrower;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BorrowerBookCommandHandler implements Command.Handler<BorrowerBookCommand, ResponseEntity<String>> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final BookRepository bookRepository;
    private final BorrowerRepository borrowerRepository;

    public BorrowerBookCommandHandler(BookRepository bookRepository, BorrowerRepository borrowerRepository) {
        this.bookRepository = bookRepository;
        this.borrowerRepository = borrowerRepository;
    }

    @Override
    public ResponseEntity<String> handle(BorrowerBookCommand command) {
        logger.debug("Finding existing borrower request to process with the id: {}", command.getBorrowerId());
        Optional<Borrower> borrower = borrowerRepository.findById(command.getBorrowerId());

        if (borrower.isEmpty()) {
            logger.debug("Borrower not found with the id: {}", command.getBorrowerId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Borrower not found");
        }

        logger.debug("Finding existing book request to process with the id: {}", command.getBookId());
        Optional<Book> book = bookRepository.findById(command.getBookId());

        if (book.isEmpty()) {
            logger.debug("Book not found with the id: {}", command.getBookId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
        }

        if (book.get().isBorrowed()) {
            logger.debug("Book with name {} is already borrowed id: {} and status {}", book.get().getTitle(),
                    command.getBookId(), book.get().isBorrowed());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Book is already borrowed");
        }

        Book bookEntity = book.get();
        bookEntity.setBorrowed(true);
        bookRepository.save(bookEntity);

        logger.debug("Book with name {} is borrowed successfully id: {} and borrowed status: {}",
                book.get().getTitle(), command.getBookId(), bookEntity.isBorrowed());
        return ResponseEntity.status(HttpStatus.OK).body("Book borrowed successfully");
    }
}

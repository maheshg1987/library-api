package com.libraryapi.controllers;

import an.awesome.pipelinr.Pipeline;
import com.libraryapi.commands.BorrowerBookCommand;
import com.libraryapi.commands.BorrowerCommand;
import com.libraryapi.commands.ReturnBookCommand;
import com.libraryapi.dao.repositories.BorrowerRepository;
import com.libraryapi.dtos.BorrowerResponseDTO;
import com.libraryapi.model.Borrower;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BorrowerController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final BorrowerRepository borrowerRepository;
    private final Pipeline pipeline;

    BorrowerController(
            BorrowerRepository borrowerRepository,
            Pipeline pipeline
    ) {
        this.borrowerRepository = borrowerRepository;
        this.pipeline = pipeline;
    }

    @Operation(summary = "Register a borrower", description = "Register a new borrower")
    @PostMapping("/borrowers/request")
    public ResponseEntity<BorrowerResponseDTO> registerBorrower(@Valid @RequestBody BorrowerCommand command, HttpServletResponse httpResponse) {
        logger.debug("Request to register a borrower for name: {}", command.getName());
        BorrowerResponseDTO borrowerResponseDTO = pipeline.send(command);
        return new ResponseEntity<>(borrowerResponseDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Borrow a book", description = "Allows a borrower to borrow a book")
    @PostMapping("/borrowers/borrow/request")
    public ResponseEntity<String> borrowBook(@Valid @RequestBody BorrowerBookCommand command) {
        logger.debug("Request to borrow a book with bookId: {} for borrowerId: {}", command.getBookId(), command.getBorrowerId());
        return pipeline.send(command);
    }

    @Operation(summary = "Return a book", description = "Allows a borrower to return a book")
    @PostMapping("/borrowers/return/request")
    public ResponseEntity<String> returnBook(@Valid @RequestBody ReturnBookCommand command) {
        logger.debug("Request to return a book with bookId: {} for borrowerId: {} ", command.getBookId(), command.getBorrowerId());
        return pipeline.send(command);
    }

    @Operation(summary = "Get all borrowers ", description = "Get all Registered borrowers")
    @GetMapping("/borrowers")
    public ResponseEntity<List<Borrower>> getAllBorrower() {
        logger.debug("Request to get all registered borrowers");
        return ResponseEntity.ok(borrowerRepository.findAll());
    }

}

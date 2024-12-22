package com.libraryapi.controllers;

import an.awesome.pipelinr.Pipeline;
import com.libraryapi.commands.BookCommand;
import com.libraryapi.dao.repositories.BookRepository;
import com.libraryapi.dtos.BookResponseDTO;
import com.libraryapi.model.Book;
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
public class BookController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BookRepository bookRepository;
    private final Pipeline pipeline;

    BookController(BookRepository bookRepository, Pipeline pipeline) {
        this.bookRepository = bookRepository;
        this.pipeline = pipeline;
    }

    @Operation(summary = "Register a book", description = "Register a new book")
    @PostMapping("/books/request")
    public ResponseEntity<BookResponseDTO> registerBook(@Valid @RequestBody BookCommand command, HttpServletResponse httpResponse) {
        logger.debug("Request to register a book for isbn: {}", command.getIsbn());
        BookResponseDTO bookResponseDTO = pipeline.send(command);
        return new ResponseEntity<>(bookResponseDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all books ", description = "Get all Registered books")
    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks() {
        logger.debug("Request to get all registered books");
        return ResponseEntity.ok(bookRepository.findAll());
    }

}

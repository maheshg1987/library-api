package com.libraryapi.commands;


import an.awesome.pipelinr.Command;
import com.libraryapi.dtos.BookResponseDTO;
import jakarta.validation.constraints.NotBlank;

public class BookCommand implements Command<BookResponseDTO> {

    @NotBlank(message = "ISBN is mandatory")
    private String isbn;

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Author is mandatory")
    private String author;

    private boolean borrowed = false;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isBorrowed() {
        return borrowed;
    }

    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }


}

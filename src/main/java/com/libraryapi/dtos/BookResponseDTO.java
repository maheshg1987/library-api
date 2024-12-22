package com.libraryapi.dtos;

import java.util.UUID;

public class BookResponseDTO {

    private UUID id;

    private String isbn;

    private String title;

    private String author;

    private boolean borrowed;

    public BookResponseDTO() {
    }


    public BookResponseDTO(UUID id, String isbn, String title, String author, boolean borrowed) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.borrowed = borrowed;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

package com.libraryapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class Book {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private String isbn;

    private String title;

    private String author;

    private boolean borrowed = false;

    public Book() {
    }

    public Book(UUID id, String isbn, String title, String author, boolean borrowed) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.borrowed = borrowed;
    }

    public Book(String isbn, String title, String author, boolean borrowed) {
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

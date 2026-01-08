package com.example.bookinventory.model;

import java.math.BigDecimal;

/**
 * Book represents the core domain entity of our system.
 * This is a simple POJO (Plain Old Java Object).
 */
public class Book {

    private Long id;                // System-generated unique identifier
    private String title;            // Book title (mandatory)
    private String author;           // Author name
    private BigDecimal price;        // Monetary value (BigDecimal is precise)
    private String isbn;             // Business identifier

    // Default constructor (required for JSON deserialization)
    public Book() {
    }

    // Parameterized constructor (useful for testing & internal use)
    public Book(Long id, String title, String author, BigDecimal price, String isbn) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.isbn = isbn;
    }

    // Getters and Setters (encapsulation)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                ", isbn='" + isbn + '\'' +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (id != null ? !id.equals(book.id) : book.id != null) return false;
        if (title != null ? !title.equals(book.title) : book.title != null) return false;
        if (author != null ? !author.equals(book.author) : book.author != null) return false;
        if (price != null ? !price.equals(book.price) : book.price != null) return false;
        return isbn != null ? isbn.equals(book.isbn) : book.isbn == null;
    }


}

package com.example.bookinventory.exception;

/**
 * Custom exception for book-not-found scenarios.
 */
public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(Long id) {
        super("Book not found with id: " + id);
    }
}

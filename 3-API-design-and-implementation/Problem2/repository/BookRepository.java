package com.example.bookinventory.repository;

import com.example.bookinventory.model.Book;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repository layer simulates database behavior using in-memory storage.
 */
@Repository
public class BookRepository {

    private final Map<Long, Book> bookStore = new HashMap<>();
    private Long currentId = 1L;

    /**
     * Saves a book and auto-generates an ID.
     */
    public Book save(Book book) {
        book.setId(currentId++);
        bookStore.put(book.getId(), book);
        return book;
    }

    /**
     * Finds a book by ID.
     */
    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(bookStore.get(id));
    }

    public List<Book> findByTitle(String title) {
        return bookStore.values().stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .collect(Collectors.toList());
    }

    public List<Book> findByAuthor(String author) {
        return bookStore.values().stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                .collect(Collectors.toList());
    }

    public List<Book> findAll() {
        return new ArrayList<>(bookStore.values());
    }

    public Book update(Long id, Book updatedBook) {
        bookStore.put(id, updatedBook);
        return updatedBook;
    }

    public void deleteById(Long id) {
        bookStore.remove(id);
    }

    public List<Book> findByPage(int page, int size) {
        return bookStore.values().stream()
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    public Book partialUpdate(Long id, Book partialBook) {
        Book existingBook = bookStore.get(id);
        if (existingBook == null) {
            return null;
        }

        if (partialBook.getTitle() != null) {
            existingBook.setTitle(partialBook.getTitle());
        }
        if (partialBook.getAuthor() != null) {
            existingBook.setAuthor(partialBook.getAuthor());
        }
        if (partialBook.getPrice() != null) {
            existingBook.setPrice(partialBook.getPrice());
        }

        bookStore.put(id, existingBook);
        return existingBook;
    }
    
}

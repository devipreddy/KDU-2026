package com.example.bookinventory.service;

import com.example.bookinventory.exception.BookNotFoundException;
import com.example.bookinventory.model.Book;
import com.example.bookinventory.repository.BookRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.Comparator;
import java.util.Map;
import java.util.LinkedHashMap;
import java.math.BigDecimal;

/**
 * Service layer contains business logic and validation.
 */
@Service
public class BookService {

    private final BookRepository bookRepository;

    // Constructor Injection (recommended)
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    public Book addBook(Book book) {
        if (book.getTitle() == null || book.getTitle().isBlank()) {
            throw new IllegalArgumentException("Book title cannot be empty");
        }

        if (book.getPrice() == null || book.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Book price cannot be negative");
        }

        return bookRepository.save(book);
    }

    /**
     * Retrieves a book by ID.
     */
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    public List<Book> getBooksByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    public List<Book> getBooksByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    // Added method for pagination - Problem 2
    public List<Book> getBooksByPage(
            int page,
            int size,
            String author,
            String sort) {

        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Invalid pagination parameters");
        }

        Stream<Book> stream = bookRepository.findAll().stream();

        // 🔹 Filter by author (if present)
        if (author != null && !author.isBlank()) {
            String authorName = author.toLowerCase();
            stream = stream.filter(book ->
                    book.getAuthor() != null &&
                    book.getAuthor().toLowerCase().contains(authorName)
            );
        }

        // 🔹 Sorting (default = id)
        Comparator<Book> comparator;

        switch (sort) {
            case "title":
                comparator = Comparator.comparing(Book::getTitle,
                        Comparator.nullsLast(String::compareToIgnoreCase));
                break;

            case "author":
                comparator = Comparator.comparing(Book::getAuthor,
                        Comparator.nullsLast(String::compareToIgnoreCase));
                break;

            case "price":
                comparator = Comparator.comparing(Book::getPrice,
                        Comparator.nullsLast(BigDecimal::compareTo));
                break;

            default:
                comparator = Comparator.comparing(Book::getId);
        }

        stream = stream.sorted(comparator);

        // 🔹 Pagination (offset + limit)
        return stream
                .skip((long) page * size)
                .limit(size)
                .toList();
    }


    public Book updateBook(Long id, Book updatedBook) {

        // 1. Check if book exists
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    
        // 2. Validate input (reuse same rules)
        if (updatedBook.getTitle() == null || updatedBook.getTitle().isBlank()) {
            throw new IllegalArgumentException("Book title cannot be empty");
        }
    
        if (updatedBook.getPrice() == null ||
            updatedBook.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Book price cannot be negative");
        }
    
        // 3. Preserve ID
        updatedBook.setId(existingBook.getId());
    
        // 4. Save updated book
        return bookRepository.update(id, updatedBook);
    }

    public void deleteBook(Long id) {

        // 1. Check if book exists
        bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    
        // 2. Delete book
        bookRepository.deleteById(id);
    }

    public Book partialUpdate(Long id, Book partialBook) {
        // 1. Retrieve existing book
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    
        // 2. Update only non-null fields
        if (partialBook.getTitle() != null) {
            existingBook.setTitle(partialBook.getTitle());
        }
        if (partialBook.getAuthor() != null) {
            existingBook.setAuthor(partialBook.getAuthor());
        }
        if (partialBook.getPrice() != null) {
            if (partialBook.getPrice().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Book price cannot be negative");
            }
            existingBook.setPrice(partialBook.getPrice());
        }
    
        // 3. Save updated book
        return bookRepository.update(id, existingBook);
    }
    
    
}

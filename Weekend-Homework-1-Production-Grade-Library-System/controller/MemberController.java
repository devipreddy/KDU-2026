package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.Optional;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponents;
import java.net.URI;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/books")
public class MemberController {

    private final BookService bookService;

    public MemberController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Create a new book", description = "Creates a new book with async processing.")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {

        Book savedBook = bookService.createBook(book); // use save method for immediate save

        URI uri = UriComponentsBuilder
                .fromPath("/books/{id}")
                .buildAndExpand(savedBook.getId())
                .toUri();

        //return ResponseEntity.created(uri).body(savedBook); // 201 Created
        return ResponseEntity.accepted().body(savedBook); // 202 Accepted
    }


    @Operation(summary = "Get all books", description = "Retrieves a list of all books.")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'MEMBER')")
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @Operation(summary = "Get book by ID", description = "Retrieves a book by its ID.")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'MEMBER')")
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete book by ID", description = "Deletes a book by its ID.")    
    @PreAuthorize("hasRole('LIBRARIAN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get book status by ID", description = "Retrieves the status of a book by its ID.")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'MEMBER')")
    @GetMapping("/status/{id}")
    public ResponseEntity<Book.Status> getBookStatus(@PathVariable Long id) {
        Optional<Book> optionalBook = bookService.findById(id);
        if (optionalBook.isPresent()) {
            return ResponseEntity.ok(optionalBook.get().getStatus());
        } else {
            return ResponseEntity.notFound().build();   
        }
    }

}

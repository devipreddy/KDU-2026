package com.example.demo.service;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    private final Object lock = new Object();
    private final ExecutorService executor = Executors.newFixedThreadPool(2);


    public Book save(Book book) {

        return bookRepository.save(book);
    }

    public Book createBook(Book book) {

        // 1️⃣ Save immediately
        book.setStatus(Book.Status.PROCESSING);
        Book saved = bookRepository.save(book);
        Long id = saved.getId();

        // 2️⃣ Submit background work
        executor.submit(() -> {
            try {
                Thread.sleep(3000);

                Book b = bookRepository.findById(id).orElseThrow();
                b.setStatus(Book.Status.AVAILABLE);
                bookRepository.save(b);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 3️⃣ Return immediately
        return saved;
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    public List<Book> findByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    public Enum<Book.Status> getBookStatus(Long id) {
        return bookRepository.findById(id)
                .map(Book::getStatus)
                .orElse(null);
    }

    public Map<String, Long> getAuditSummary() {
    return bookRepository.findAll()
            .stream()
            .collect(Collectors.groupingBy(
                    book -> book.getStatus().name(),
                    Collectors.counting()
            ));
    }
}

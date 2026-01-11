package com.example.demo.repository;

import com.example.demo.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    // Derived queries (Spring builds SQL automatically)
    List<Book> findByAuthor(String author);



    List<Book> findByTitleContainingIgnoreCase(String title);
}

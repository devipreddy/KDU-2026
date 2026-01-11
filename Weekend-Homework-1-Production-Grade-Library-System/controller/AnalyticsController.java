package com.example.demo.controller;

import com.example.demo.service.BookService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.stream.Collectors;
import io.swagger.v3.oas.annotations.Operation;
@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final BookService bookService;

    public AnalyticsController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Get audit summary", description = "Retrieves a summary of book statuses.")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("/audit")
    public ResponseEntity<Map<String, Long>> audit() {
        return ResponseEntity.ok(bookService.getAuditSummary());
    }
}

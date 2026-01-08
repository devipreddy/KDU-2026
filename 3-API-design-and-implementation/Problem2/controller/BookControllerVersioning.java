import com.example.bookinventory.model.Book;
import com.example.bookinventory.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;


@RestController
@RequestMapping("/api/v1/books")
public class BookControllerVersioning {   

    @Autowired
    private BookService bookService;

    @PostMapping
    public ResponseEntity<Book> addNewBook(@RequestBody Book book) {
        Book newBook = bookService.addBook(book);

        Long bookId = newBook.getId();

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(bookId)
                .toUri();
        
        return ResponseEntity.created(location).body(newBook);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(){
        List<Book> books = bookService.getAllBooks();
        return books.isEmpty()
            ? ResponseEntity.ok(Collections.emptyList())
            : ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public EntityModel<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        return EntityModel.of(book,
        linkTo(methodOn(BookController.class).getBookById(id)).withSelfRel(),
        linkTo(methodOn(BookController.class).getAllBooks()).withRel("all-books")); 
    }

    

    //Added pagination endpoint - Problem 2
    @GetMapping(params = {"page", "size", "author", "sort"})
    public ResponseEntity<List<Book>> getBooksbyPage(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            @RequestParam("author") Optional<String> author,
            @RequestParam("sort") Optional<String> sort) {
        
        int pageNumber = page.orElse(0);
        int pageSize = size.orElse(10);
        String sortBy = sort.orElse("id");
        String authorName = author.orElse("");

        List<Book> books = bookService.getBooksByPage(pageNumber, pageSize, authorName, sortBy);
        
        return books.isEmpty()
            ? ResponseEntity.ok(Collections.emptyList())
            : ResponseEntity.ok(books);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updatewholeBook(@PathVariable Long id, @RequestBody Book book) {
        Book updatedBook = bookService.updateBook(id, book);

        if (updatedBook == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedBook);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookbyId(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

}
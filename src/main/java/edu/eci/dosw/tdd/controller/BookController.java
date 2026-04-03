package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final LibraryService libraryService;

    @GetMapping
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'USER')")
    public List<Book> getAllBooks() {
        return libraryService.getAllBooks();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'USER')")
    public Book getBookById(@PathVariable String id) {
        return libraryService.getBookById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('LIBRARIAN')")
    public void addBook(@RequestBody Book book, @RequestParam int quantity) {
        libraryService.addBook(book, quantity);
    }
}

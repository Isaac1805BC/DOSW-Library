package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.ResourceNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LibraryServiceTest {

    private LibraryService libraryService;

    @BeforeEach
    void setUp() {
        libraryService = new LibraryService();
    }

    // ========== Tests para Libros ==========

    @Test
    void addBookAndGetAll() {
        Book book = new Book("1", "Clean Code", "Robert C. Martin");
        libraryService.addBook(book, 3);

        List<Book> books = libraryService.getAllBooks();

        assertEquals(1, books.size());
        assertEquals("Clean Code", books.get(0).getTitle());
    }

    @Test
    void addBookIncreasesQuantity() {
        Book book = new Book("1", "Clean Code", "Robert C. Martin");
        libraryService.addBook(book, 2);
        libraryService.addBook(book, 3);

        List<Book> books = libraryService.getAllBooks();

        assertEquals(1, books.size());
    }

    @Test
    void getBookByIdSuccess() {
        Book book = new Book("1", "Clean Code", "Robert C. Martin");
        libraryService.addBook(book, 1);

        Book found = libraryService.getBookById("1");

        assertEquals("1", found.getId());
        assertEquals("Clean Code", found.getTitle());
        assertEquals("Robert C. Martin", found.getAuthor());
    }

    @Test
    void getBookByIdNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> {
            libraryService.getBookById("999");
        });
    }

    // ========== Tests para Usuarios ==========

    @Test
    void registerUserAndGetAll() {
        User user = new User("1", "Isaac");
        libraryService.registerUser(user);

        List<User> users = libraryService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals("Isaac", users.get(0).getName());
    }

    @Test
    void getUserByIdSuccess() {
        User user = new User("1", "Isaac");
        libraryService.registerUser(user);

        User found = libraryService.getUserById("1");

        assertEquals("1", found.getId());
        assertEquals("Isaac", found.getName());
    }

    @Test
    void getUserByIdNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> {
            libraryService.getUserById("999");
        });
    }
}

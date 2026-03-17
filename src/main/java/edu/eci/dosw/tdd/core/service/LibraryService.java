package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.ResourceNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LibraryService {

    private final List<User> users;
    private final List<Loan> loans;
    private final Map<Book, Integer> books;

    public LibraryService() {
        this.users = new ArrayList<>();
        this.loans = new ArrayList<>();
        this.books = new HashMap<>();
    }

    public void addBook(Book book, int quantity) {
        books.put(book, books.getOrDefault(book, 0) + quantity);
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books.keySet());
    }

    public Book getBookById(String id) {
        return books.keySet().stream()
                .filter(book -> book.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));
    }

    public void registerUser(User user) {
        users.add(user);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public User getUserById(String id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }
}

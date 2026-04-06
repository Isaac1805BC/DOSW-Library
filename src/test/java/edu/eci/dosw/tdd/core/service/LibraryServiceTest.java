package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.exception.ResourceNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.Role;
import edu.eci.dosw.tdd.core.model.Status;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.port.IBookRepository;
import edu.eci.dosw.tdd.core.port.ILoanRepository;
import edu.eci.dosw.tdd.core.port.IUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {

    @Mock private IBookRepository bookRepository;
    @Mock private IUserRepository userRepository;
    @Mock private ILoanRepository loanRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LibraryService libraryService;

    // ─── Book Tests ───────────────────────────────────────────────────────────

    @Test
    void addBookAndGetAll() {
        Book book = new Book("1", "Clean Code", "Robert C. Martin", "978-0-13", 0, 0, null, null, null, null, null, null);
        Book saved = new Book("1", "Clean Code", "Robert C. Martin", "978-0-13", 3, 3, null, null, null, null, null, null);

        when(bookRepository.findById("1")).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenReturn(saved);
        when(bookRepository.findAll()).thenReturn(List.of(saved));

        libraryService.addBook(book, 3);
        List<Book> books = libraryService.getAllBooks();

        assertEquals(1, books.size());
        assertEquals("Clean Code", books.get(0).getTitle());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void addBookIncreasesQuantityWhenExists() {
        Book existing = new Book("1", "Clean Code", "Robert C. Martin", "978-0-13", 2, 2, null, null, null, null, null, null);

        when(bookRepository.findById("1")).thenReturn(Optional.of(existing));
        when(bookRepository.save(any(Book.class))).thenReturn(existing);

        Book book = new Book("1", "Clean Code", "Robert C. Martin", "978-0-13", 0, 0, null, null, null, null, null, null);
        libraryService.addBook(book, 3);

        assertEquals(5, existing.getTotalQuantity());
        assertEquals(5, existing.getAvailableQuantity());
        verify(bookRepository).save(existing);
    }

    @Test
    void getBookByIdSuccess() {
        Book book = new Book("1", "Clean Code", "Robert C. Martin", "978-0-13", 1, 1, null, null, null, null, null, null);

        when(bookRepository.findById("1")).thenReturn(Optional.of(book));

        Book found = libraryService.getBookById("1");

        assertEquals("1", found.getId());
        assertEquals("Clean Code", found.getTitle());
    }

    @Test
    void getBookByIdNotFound() {
        when(bookRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> libraryService.getBookById("999"));
    }

    // ─── User Tests ───────────────────────────────────────────────────────────

    @Test
    void registerUserAndGetAll() {
        User user = new User("1", "Isaac", "isaac@lib.com", "plaintext", Role.USER, null, null);
        User saved = new User("1", "Isaac", "isaac@lib.com", "hashed", Role.USER, null, null);

        when(passwordEncoder.encode("plaintext")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenReturn(saved);
        when(userRepository.findAll()).thenReturn(List.of(saved));

        libraryService.registerUser(user);
        List<User> users = libraryService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals("Isaac", users.get(0).getName());
        verify(passwordEncoder).encode(anyString());
    }

    @Test
    void getUserByIdSuccess() {
        User user = new User("1", "Isaac", "isaac@lib.com", null, Role.USER, null, null);

        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        User found = libraryService.getUserById("1");

        assertEquals("1", found.getId());
        assertEquals("Isaac", found.getName());
    }

    @Test
    void getUserByIdNotFound() {
        when(userRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> libraryService.getUserById("999"));
    }

    // ─── Loan Tests ───────────────────────────────────────────────────────────

    @Test
    void createLoanSuccess() {
        Book book = new Book("B1", "Clean Code", "Martin", "978", 2, 2, null, null, null, null, null, null);
        User user = new User("U1", "Isaac", "isaac@lib.com", "hash", Role.USER, null, null);
        Loan savedLoan = new Loan("loan-id", book, user, LocalDate.now(), Status.ACTIVE, null, null);

        when(bookRepository.findById("B1")).thenReturn(Optional.of(book));
        when(userRepository.findById("U1")).thenReturn(Optional.of(user));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(loanRepository.save(any(Loan.class))).thenReturn(savedLoan);

        Loan result = libraryService.createLoan("B1", "U1");

        assertNotNull(result);
        assertEquals(Status.ACTIVE, result.getStatus());
        assertEquals(1, book.getAvailableQuantity());
    }

    @Test
    void createLoanBookNotAvailable() {
        Book book = new Book("B1", "Clean Code", "Martin", "978", 1, 0, null, null, null, null, null, null);
        User user = new User("U1", "Isaac", "isaac@lib.com", "hash", Role.USER, null, null);

        when(bookRepository.findById("B1")).thenReturn(Optional.of(book));
        when(userRepository.findById("U1")).thenReturn(Optional.of(user));

        assertThrows(BookNotAvailableException.class, () -> libraryService.createLoan("B1", "U1"));
    }

    @Test
    void returnLoanSuccess() {
        Book book = new Book("B1", "Clean Code", "Martin", "978", 2, 1, null, null, null, null, null, null);
        User user = new User("U1", "Isaac", "isaac@lib.com", "hash", Role.USER, null, null);
        Loan loan = new Loan("L1", book, user, LocalDate.now().minusDays(1), Status.ACTIVE, null, null);
        Loan returned = new Loan("L1", book, user, loan.getLoanDate(), Status.RETURNED, LocalDate.now(), null);

        when(loanRepository.findById("L1")).thenReturn(Optional.of(loan));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(loanRepository.save(any(Loan.class))).thenReturn(returned);

        Loan result = libraryService.returnLoan("L1");

        assertEquals(Status.RETURNED, result.getStatus());
        assertEquals(2, book.getAvailableQuantity());
        assertNotNull(result.getReturnDate());
    }

    @Test
    void returnLoanNotActive() {
        Book book = new Book("B1", "Clean Code", "Martin", "978", 2, 2, null, null, null, null, null, null);
        User user = new User("U1", "Isaac", "isaac@lib.com", "hash", Role.USER, null, null);
        Loan loan = new Loan("L1", book, user, LocalDate.now().minusDays(5), Status.RETURNED, LocalDate.now().minusDays(1), null);

        when(loanRepository.findById("L1")).thenReturn(Optional.of(loan));

        assertThrows(IllegalArgumentException.class, () -> libraryService.returnLoan("L1"));
    }

    @Test
    void returnLoanDoesNotExceedTotalQuantity() {
        Book book = new Book("B1", "Clean Code", "Martin", "978", 2, 2, null, null, null, null, null, null);
        User user = new User("U1", "Isaac", "isaac@lib.com", "hash", Role.USER, null, null);
        Loan loan = new Loan("L1", book, user, LocalDate.now().minusDays(1), Status.ACTIVE, null, null);
        Loan returned = new Loan("L1", book, user, loan.getLoanDate(), Status.RETURNED, LocalDate.now(), null);

        when(loanRepository.findById("L1")).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(Loan.class))).thenReturn(returned);

        libraryService.returnLoan("L1");

        verify(bookRepository, never()).save(any(Book.class));
        assertEquals(2, book.getAvailableQuantity());
    }

    // ─── 5 Nuevos Tests (Reto #6) ─────────────────────────────────────────────

    @Test
    void getLoanByIdSuccess() {
        Book book = new Book("B1", "Clean Code", "Martin", "978", 2, 1, null, null, null, null, null, null);
        User user = new User("U1", "Isaac", "isaac@lib.com", null, Role.USER, null, null);
        Loan loan = new Loan("L1", book, user, LocalDate.now(), Status.ACTIVE, null, null);

        when(loanRepository.findById("L1")).thenReturn(Optional.of(loan));

        Loan result = libraryService.getLoanById("L1");

        assertEquals("L1", result.getId());
    }

    @Test
    void getAllLoansReturnsEmpty() {
        when(loanRepository.findAll()).thenReturn(Collections.emptyList());

        List<Loan> result = libraryService.getAllLoans();

        assertTrue(result.isEmpty());
    }

    @Test
    void createLoanFromEmptyState() {
        Book book = new Book("B2", "Refactoring", "Fowler", "978-2", 1, 1, null, null, null, null, null, null);
        User user = new User("U2", "Maria", "maria@lib.com", "hash", Role.USER, null, null);
        Loan savedLoan = new Loan("loan-new", book, user, LocalDate.now(), Status.ACTIVE, null, null);

        when(bookRepository.findById("B2")).thenReturn(Optional.of(book));
        when(userRepository.findById("U2")).thenReturn(Optional.of(user));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(loanRepository.save(any(Loan.class))).thenReturn(savedLoan);

        Loan result = libraryService.createLoan("B2", "U2");

        assertNotNull(result);
        assertEquals("loan-new", result.getId());
        assertEquals(Status.ACTIVE, result.getStatus());
    }

    @Test
    void deleteLoanSuccess() {
        Book book = new Book("B1", "Clean Code", "Martin", "978", 2, 2, null, null, null, null, null, null);
        User user = new User("U1", "Isaac", "isaac@lib.com", null, Role.USER, null, null);
        Loan loan = new Loan("L1", book, user, LocalDate.now(), Status.ACTIVE, null, null);

        when(loanRepository.findById("L1")).thenReturn(Optional.of(loan));
        doNothing().when(loanRepository).deleteById("L1");

        assertDoesNotThrow(() -> libraryService.deleteLoan("L1"));
        verify(loanRepository).deleteById("L1");
    }

    @Test
    void deleteLoanAndGetAllReturnsEmpty() {
        Book book = new Book("B1", "Clean Code", "Martin", "978", 2, 2, null, null, null, null, null, null);
        User user = new User("U1", "Isaac", "isaac@lib.com", null, Role.USER, null, null);
        Loan loan = new Loan("L1", book, user, LocalDate.now(), Status.ACTIVE, null, null);

        when(loanRepository.findById("L1")).thenReturn(Optional.of(loan));
        doNothing().when(loanRepository).deleteById("L1");
        when(loanRepository.findAll()).thenReturn(Collections.emptyList());

        libraryService.deleteLoan("L1");
        List<Loan> result = libraryService.getAllLoans();

        assertTrue(result.isEmpty());
    }
}

package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.exception.ResourceNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.Role;
import edu.eci.dosw.tdd.core.model.Status;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.entity.BookEntity;
import edu.eci.dosw.tdd.persistence.entity.LoanEntity;
import edu.eci.dosw.tdd.persistence.entity.UserEntity;
import edu.eci.dosw.tdd.persistence.mapper.BookMapper;
import edu.eci.dosw.tdd.persistence.mapper.LoanMapper;
import edu.eci.dosw.tdd.persistence.mapper.UserMapper;
import edu.eci.dosw.tdd.persistence.repository.BookRepository;
import edu.eci.dosw.tdd.persistence.repository.LoanRepository;
import edu.eci.dosw.tdd.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {

    @Mock private BookRepository bookRepository;
    @Mock private UserRepository userRepository;
    @Mock private LoanRepository loanRepository;
    @Mock private BookMapper bookMapper;
    @Mock private UserMapper userMapper;
    @Mock private LoanMapper loanMapper;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LibraryService libraryService;

    // ==================== BOOK TESTS ====================

    @Test
    void addBookAndGetAll() {
        Book book = new Book("1", "Clean Code", "Robert C. Martin", "978-0-13", 0, 0);
        BookEntity entity = new BookEntity("1", "Clean Code", "Robert C. Martin", "978-0-13", 3, 3);

        when(bookRepository.findById("1")).thenReturn(Optional.empty());
        when(bookMapper.toEntity(any(Book.class))).thenReturn(entity);
        when(bookRepository.save(entity)).thenReturn(entity);
        when(bookRepository.findAll()).thenReturn(List.of(entity));
        when(bookMapper.toModelList(List.of(entity))).thenReturn(List.of(book));

        libraryService.addBook(book, 3);
        List<Book> books = libraryService.getAllBooks();

        assertEquals(1, books.size());
        assertEquals("Clean Code", books.get(0).getTitle());
        verify(bookRepository).save(any(BookEntity.class));
    }

    @Test
    void addBookIncreasesQuantityWhenExists() {
        BookEntity existing = new BookEntity("1", "Clean Code", "Robert C. Martin", "978-0-13", 2, 2);

        when(bookRepository.findById("1")).thenReturn(Optional.of(existing));
        when(bookRepository.save(existing)).thenReturn(existing);

        Book book = new Book("1", "Clean Code", "Robert C. Martin", "978-0-13", 0, 0);
        libraryService.addBook(book, 3);

        assertEquals(5, existing.getCantidadTotal());
        assertEquals(5, existing.getCantidadDisponible());
        verify(bookRepository).save(existing);
    }

    @Test
    void getBookByIdSuccess() {
        BookEntity entity = new BookEntity("1", "Clean Code", "Robert C. Martin", "978-0-13", 1, 1);
        Book book = new Book("1", "Clean Code", "Robert C. Martin", "978-0-13", 1, 1);

        when(bookRepository.findById("1")).thenReturn(Optional.of(entity));
        when(bookMapper.toModel(entity)).thenReturn(book);

        Book found = libraryService.getBookById("1");

        assertEquals("1", found.getId());
        assertEquals("Clean Code", found.getTitle());
    }

    @Test
    void getBookByIdNotFound() {
        when(bookRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> libraryService.getBookById("999"));
    }

    // ==================== USER TESTS ====================

    @Test
    void registerUserAndGetAll() {
        User user = new User("1", "Isaac", "isaac@lib.com", "plaintext", Role.USER);
        UserEntity entity = new UserEntity("1", "Isaac", "isaac@lib.com", "hashed", Role.USER);

        when(passwordEncoder.encode("plaintext")).thenReturn("hashed");
        when(userMapper.toEntity(any(User.class))).thenReturn(entity);
        when(userRepository.save(entity)).thenReturn(entity);
        when(userRepository.findAll()).thenReturn(List.of(entity));
        when(userMapper.toModelList(List.of(entity))).thenReturn(List.of(user));

        libraryService.registerUser(user);
        List<User> users = libraryService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals("Isaac", users.get(0).getName());
        verify(passwordEncoder).encode(anyString());
    }

    @Test
    void getUserByIdSuccess() {
        UserEntity entity = new UserEntity("1", "Isaac", "isaac@lib.com", "hashed", Role.USER);
        User user = new User("1", "Isaac", "isaac@lib.com", null, Role.USER);

        when(userRepository.findById("1")).thenReturn(Optional.of(entity));
        when(userMapper.toModel(entity)).thenReturn(user);

        User found = libraryService.getUserById("1");

        assertEquals("1", found.getId());
        assertEquals("Isaac", found.getName());
    }

    @Test
    void getUserByIdNotFound() {
        when(userRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> libraryService.getUserById("999"));
    }

    // ==================== LOAN TESTS ====================

    @Test
    void createLoanSuccess() {
        BookEntity book = new BookEntity("B1", "Clean Code", "Martin", "978", 2, 2);
        UserEntity user = new UserEntity("U1", "Isaac", "isaac@lib.com", "hash", Role.USER);
        LoanEntity savedLoan = new LoanEntity(null, book, user, LocalDate.now(), Status.ACTIVE, null);
        Loan loanModel = new Loan("loan-id", new Book("B1", "Clean Code", "Martin", "978", 2, 1),
                new User("U1", "Isaac", "isaac@lib.com", null, Role.USER), LocalDate.now(), Status.ACTIVE, null);

        when(bookRepository.findById("B1")).thenReturn(Optional.of(book));
        when(userRepository.findById("U1")).thenReturn(Optional.of(user));
        when(bookRepository.save(book)).thenReturn(book);
        when(loanRepository.save(any(LoanEntity.class))).thenReturn(savedLoan);
        when(loanMapper.toModel(savedLoan)).thenReturn(loanModel);

        Loan result = libraryService.createLoan("B1", "U1");

        assertNotNull(result);
        assertEquals(Status.ACTIVE, result.getStatus());
        assertEquals(1, book.getCantidadDisponible());
    }

    @Test
    void createLoanBookNotAvailable() {
        BookEntity book = new BookEntity("B1", "Clean Code", "Martin", "978", 1, 0);
        UserEntity user = new UserEntity("U1", "Isaac", "isaac@lib.com", "hash", Role.USER);

        when(bookRepository.findById("B1")).thenReturn(Optional.of(book));
        when(userRepository.findById("U1")).thenReturn(Optional.of(user));

        assertThrows(BookNotAvailableException.class, () -> libraryService.createLoan("B1", "U1"));
    }

    @Test
    void returnLoanSuccess() {
        BookEntity book = new BookEntity("B1", "Clean Code", "Martin", "978", 2, 1);
        UserEntity user = new UserEntity("U1", "Isaac", "isaac@lib.com", "hash", Role.USER);
        LoanEntity loan = new LoanEntity("L1", book, user, LocalDate.now().minusDays(1), Status.ACTIVE, null);
        Loan returnedModel = new Loan("L1", null, null, loan.getLoanDate(), Status.RETURNED, LocalDate.now());

        when(loanRepository.findById("L1")).thenReturn(Optional.of(loan));
        when(bookRepository.save(book)).thenReturn(book);
        when(loanRepository.save(loan)).thenReturn(loan);
        when(loanMapper.toModel(loan)).thenReturn(returnedModel);

        Loan result = libraryService.returnLoan("L1");

        assertEquals(Status.RETURNED, result.getStatus());
        assertEquals(2, book.getCantidadDisponible()); // restored
        assertNotNull(result.getReturnDate());
    }

    @Test
    void returnLoanNotActive() {
        BookEntity book = new BookEntity("B1", "Clean Code", "Martin", "978", 2, 2);
        UserEntity user = new UserEntity("U1", "Isaac", "isaac@lib.com", "hash", Role.USER);
        LoanEntity loan = new LoanEntity("L1", book, user, LocalDate.now().minusDays(5), Status.RETURNED, LocalDate.now().minusDays(1));

        when(loanRepository.findById("L1")).thenReturn(Optional.of(loan));

        assertThrows(IllegalArgumentException.class, () -> libraryService.returnLoan("L1"));
    }

    @Test
    void returnLoanDoesNotExceedCantidadTotal() {
        BookEntity book = new BookEntity("B1", "Clean Code", "Martin", "978", 2, 2);
        UserEntity user = new UserEntity("U1", "Isaac", "isaac@lib.com", "hash", Role.USER);
        LoanEntity loan = new LoanEntity("L1", book, user, LocalDate.now().minusDays(1), Status.ACTIVE, null);
        Loan returnedModel = new Loan("L1", null, null, loan.getLoanDate(), Status.RETURNED, LocalDate.now());

        when(loanRepository.findById("L1")).thenReturn(Optional.of(loan));
        when(loanRepository.save(loan)).thenReturn(loan);
        when(loanMapper.toModel(loan)).thenReturn(returnedModel);

        libraryService.returnLoan("L1");

        // cantidadDisponible (2) == cantidadTotal (2), so no save on book
        verify(bookRepository, never()).save(any(BookEntity.class));
        assertEquals(2, book.getCantidadDisponible()); // unchanged
    }
}

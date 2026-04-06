package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.exception.ResourceNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.Status;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.port.IBookRepository;
import edu.eci.dosw.tdd.core.port.ILoanRepository;
import edu.eci.dosw.tdd.core.port.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private final IBookRepository bookRepository;
    private final IUserRepository userRepository;
    private final ILoanRepository loanRepository;
    private final PasswordEncoder passwordEncoder;

    public void addBook(Book book, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        Book existing = bookRepository.findById(book.getId()).orElse(null);
        if (existing != null) {
            existing.setTotalQuantity(existing.getTotalQuantity() + quantity);
            existing.setAvailableQuantity(existing.getAvailableQuantity() + quantity);
            bookRepository.save(existing);
        } else {
            book.setTotalQuantity(quantity);
            book.setAvailableQuantity(quantity);
            bookRepository.save(book);
        }
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));
    }

    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    public Loan createLoan(String bookId, String userId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + bookId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        if (book.getAvailableQuantity() <= 0) {
            throw new BookNotAvailableException("Book is not available: " + book.getTitle());
        }

        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        bookRepository.save(book);

        Loan loan = new Loan();
        loan.setId(UUID.randomUUID().toString());
        loan.setBook(book);
        loan.setUser(user);
        loan.setLoanDate(LocalDate.now());
        loan.setStatus(Status.ACTIVE);

        return loanRepository.save(loan);
    }

    public Loan returnLoan(String loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with ID: " + loanId));

        if (loan.getStatus() != Status.ACTIVE) {
            throw new IllegalArgumentException("Loan is not active and cannot be returned");
        }

        loan.setStatus(Status.RETURNED);
        loan.setReturnDate(LocalDate.now());

        Book book = loan.getBook();
        if (book.getAvailableQuantity() < book.getTotalQuantity()) {
            book.setAvailableQuantity(book.getAvailableQuantity() + 1);
            bookRepository.save(book);
        }

        return loanRepository.save(loan);
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public Loan getLoanById(String id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with ID: " + id));
    }

    public void deleteLoan(String id) {
        loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with ID: " + id));
        loanRepository.deleteById(id);
    }

    public List<Loan> getLoansByUserId(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }
        return loanRepository.findByUserId(userId);
    }
}

package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.exception.ResourceNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
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
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final LoanRepository loanRepository;

    private final BookMapper bookMapper;
    private final UserMapper userMapper;
    private final LoanMapper loanMapper;

    private final PasswordEncoder passwordEncoder;

    // ==================== BOOKS ====================

    @Transactional
    public void addBook(Book book, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        BookEntity entity = bookRepository.findById(book.getId()).orElse(null);
        if (entity != null) {
            entity.setCantidadTotal(entity.getCantidadTotal() + quantity);
            entity.setCantidadDisponible(entity.getCantidadDisponible() + quantity);
            bookRepository.save(entity);
        } else {
            book.setCantidadTotal(quantity);
            book.setCantidadDisponible(quantity);
            bookRepository.save(bookMapper.toEntity(book));
        }
    }

    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookMapper.toModelList(bookRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Book getBookById(String id) {
        return bookRepository.findById(id)
                .map(bookMapper::toModel)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));
    }

    // ==================== USERS ====================

    @Transactional
    public void registerUser(User user) {
        // Hash the plain-text password before persisting
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(userMapper.toEntity(user));
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userMapper.toModelList(userRepository.findAll());
    }

    @Transactional(readOnly = true)
    public User getUserById(String id) {
        return userRepository.findById(id)
                .map(userMapper::toModel)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    // ==================== LOANS ====================

    @Transactional
    public Loan createLoan(String bookId, String userId) {
        BookEntity book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + bookId));

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        if (book.getCantidadDisponible() <= 0) {
            throw new BookNotAvailableException("Book is not available: " + book.getTitle());
        }

        book.setCantidadDisponible(book.getCantidadDisponible() - 1);
        bookRepository.save(book);

        LoanEntity loan = new LoanEntity();
        loan.setBook(book);
        loan.setUser(user);
        loan.setLoanDate(LocalDate.now());
        loan.setStatus(Status.ACTIVE);

        return loanMapper.toModel(loanRepository.save(loan));
    }

    @Transactional
    public Loan returnLoan(String loanId) {
        LoanEntity loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with ID: " + loanId));

        if (loan.getStatus() != Status.ACTIVE) {
            throw new IllegalArgumentException("Loan is not active and cannot be returned");
        }

        loan.setStatus(Status.RETURNED);
        loan.setReturnDate(LocalDate.now());

        BookEntity book = loan.getBook();
        // Guard: never exceed cantidadTotal
        if (book.getCantidadDisponible() < book.getCantidadTotal()) {
            book.setCantidadDisponible(book.getCantidadDisponible() + 1);
            bookRepository.save(book);
        }

        return loanMapper.toModel(loanRepository.save(loan));
    }

    @Transactional(readOnly = true)
    public List<Loan> getAllLoans() {
        return loanMapper.toModelList(loanRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<Loan> getLoansByUserId(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }
        return loanMapper.toModelList(loanRepository.findByUser_Id(userId));
    }
}

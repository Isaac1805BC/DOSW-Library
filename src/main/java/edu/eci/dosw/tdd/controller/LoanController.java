package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.service.LibraryService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LibraryService libraryService;

    public LoanController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    /**
     * LIBRARIAN: view all loans in the system.
     */
    @GetMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public List<Loan> getAllLoans() {
        return libraryService.getAllLoans();
    }

    /**
     * USER: view only their own loans.
     * LIBRARIAN: can view any user's loans.
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('LIBRARIAN') or #userId == authentication.name")
    public List<Loan> getLoansByUser(@PathVariable String userId) {
        return libraryService.getLoansByUserId(userId);
    }

    /**
     * Both roles can create a loan, but a USER should only borrow for themselves
     * (enforced at the client/frontend level; server validates availability).
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public Loan createLoan(@RequestParam String bookId, @RequestParam String userId) {
        return libraryService.createLoan(bookId, userId);
    }

    /**
     * Both roles can return a loan.
     */
    @PutMapping("/{loanId}/return")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'USER')")
    public Loan returnLoan(@PathVariable String loanId) {
        return libraryService.returnLoan(loanId);
    }
}

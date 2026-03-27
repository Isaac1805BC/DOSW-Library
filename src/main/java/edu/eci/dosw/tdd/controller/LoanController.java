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

    @GetMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public List<Loan> getAllLoans() {
        return libraryService.getAllLoans();
    }

    
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('LIBRARIAN') or #userId == authentication.name")
    public List<Loan> getLoansByUser(@PathVariable String userId) {
        return libraryService.getLoansByUserId(userId);
    }

    
    @PostMapping
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public Loan createLoan(@RequestParam String bookId, @RequestParam String userId) {
        return libraryService.createLoan(bookId, userId);
    }

    
    @PutMapping("/{loanId}/return")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'USER')")
    public Loan returnLoan(@PathVariable String loanId) {
        return libraryService.returnLoan(loanId);
    }
}

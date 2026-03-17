package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.service.LibraryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LibraryService libraryService;

    public LoanController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @PostMapping
    public Loan createLoan(@RequestParam String bookId, @RequestParam String userId) {
        return libraryService.createLoan(bookId, userId);
    }
}

package edu.eci.dosw.tdd.core.port;

import edu.eci.dosw.tdd.core.model.Loan;

import java.util.List;
import java.util.Optional;

public interface ILoanRepository {
    Loan save(Loan loan);
    Optional<Loan> findById(String id);
    List<Loan> findAll();
    List<Loan> findByUserId(String userId);
    void deleteById(String id);
}

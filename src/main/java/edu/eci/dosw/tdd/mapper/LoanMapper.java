package edu.eci.dosw.tdd.mapper;

import edu.eci.dosw.tdd.dto.LoanDTO;
import edu.eci.dosw.tdd.core.model.Loan;

public class LoanMapper {
    public static LoanDTO toDTO(Loan loan) {
        return new LoanDTO();
    }
}

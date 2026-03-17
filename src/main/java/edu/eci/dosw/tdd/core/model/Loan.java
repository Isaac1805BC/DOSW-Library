package edu.eci.dosw.tdd.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
    private String id;
    private User user;
    private Book book;
    private LocalDateTime loanDate;
    private LocalDateTime returnDate;
    private String status;
}

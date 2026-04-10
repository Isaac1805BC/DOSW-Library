package edu.eci.dosw.tdd.core.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanHistoryEntry {
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDate executedAt;
}

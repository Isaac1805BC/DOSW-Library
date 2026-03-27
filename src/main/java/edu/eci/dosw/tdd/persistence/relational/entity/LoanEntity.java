package edu.eci.dosw.tdd.persistence.relational.entity;

import edu.eci.dosw.tdd.core.model.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "loans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanEntity {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private BookEntity book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private LocalDate loanDate;

    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    private Status status;
}

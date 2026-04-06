package edu.eci.dosw.tdd.persistence.nonrelational.document;

import edu.eci.dosw.tdd.core.model.LoanHistoryEntry;
import edu.eci.dosw.tdd.core.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "loans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanDocument {

    @Id
    private String id;
    private BookDocument book;
    private UserDocument user;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private Status status;
    private List<LoanHistoryEntry> history;
}

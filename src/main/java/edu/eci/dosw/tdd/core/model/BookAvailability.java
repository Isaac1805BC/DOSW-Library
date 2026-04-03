package edu.eci.dosw.tdd.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookAvailability {
    private Status status;
    private int totalCopies;
    private int availableCopies;
    private int borrowedCopies;
}

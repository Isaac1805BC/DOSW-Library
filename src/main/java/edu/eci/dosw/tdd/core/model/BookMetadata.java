package edu.eci.dosw.tdd.core.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookMetadata {
    private int pages;
    private String language;
    private String publisher;
}

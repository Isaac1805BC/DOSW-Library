package edu.eci.dosw.tdd.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private String id;
    private String title;
    private String author;
    private String isbn;
    private int totalQuantity;
    private int availableQuantity;
    private List<String> categories;
    private PublicationType publicationType;
    private LocalDate publicationDate;
    private BookMetadata metadata;
    private BookAvailability availability;
    private LocalDate addedAt;
}

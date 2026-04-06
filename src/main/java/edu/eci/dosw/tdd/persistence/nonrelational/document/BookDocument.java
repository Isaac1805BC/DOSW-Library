package edu.eci.dosw.tdd.persistence.nonrelational.document;

import edu.eci.dosw.tdd.core.model.BookAvailability;
import edu.eci.dosw.tdd.core.model.BookMetadata;
import edu.eci.dosw.tdd.core.model.PublicationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDocument {

    @Id
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

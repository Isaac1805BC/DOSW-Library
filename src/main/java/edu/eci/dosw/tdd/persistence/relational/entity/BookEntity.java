package edu.eci.dosw.tdd.persistence.relational.entity;

import edu.eci.dosw.tdd.core.model.BookMetadata;
import edu.eci.dosw.tdd.core.model.PublicationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column
    private String isbn;

    @Column(nullable = false)
    private int totalQuantity;

    @Column(nullable = false)
    private int availableQuantity;

    @ElementCollection
    @CollectionTable(name = "book_categories", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "category")
    private List<String> categories;

    @Enumerated(EnumType.STRING)
    private PublicationType publicationType;

    private LocalDate publicationDate;

    private LocalDate addedAt;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "pages", column = @Column(name = "metadata_pages")),
        @AttributeOverride(name = "language", column = @Column(name = "metadata_language")),
        @AttributeOverride(name = "publisher", column = @Column(name = "metadata_publisher"))
    })
    private BookMetadata metadata;
}

package edu.eci.dosw.tdd.controller.dto;

import edu.eci.dosw.tdd.core.model.BookMetadata;
import edu.eci.dosw.tdd.core.model.PublicationType;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.List;

public record BookRequest(
        @NotBlank String title,
        @NotBlank String author,
        String isbn,
        PublicationType publicationType,
        LocalDate publicationDate,
        List<String> categories,
        BookMetadata metadata
) {}

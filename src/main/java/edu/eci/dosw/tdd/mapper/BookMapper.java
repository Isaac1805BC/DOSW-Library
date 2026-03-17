package edu.eci.dosw.tdd.mapper;

import edu.eci.dosw.tdd.dto.BookDTO;
import edu.eci.dosw.tdd.core.model.Book;

public class BookMapper {
    public static BookDTO toDTO(Book book) {
        return new BookDTO();
    }
}

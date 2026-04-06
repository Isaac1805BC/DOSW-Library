package edu.eci.dosw.tdd.persistence.nonrelational.mapper;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.persistence.nonrelational.document.BookDocument;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookDocumentMapper {

    BookDocument toDocument(Book book);

    Book toDomain(BookDocument document);

    List<Book> toDomainList(List<BookDocument> documents);
}

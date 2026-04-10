package edu.eci.dosw.tdd.persistence.relational.mapper;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.persistence.relational.entity.BookEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookEntity toEntity(Book book);

    @Mapping(target = "availability", ignore = true)
    Book toModel(BookEntity entity);

    List<Book> toModelList(List<BookEntity> entities);
}

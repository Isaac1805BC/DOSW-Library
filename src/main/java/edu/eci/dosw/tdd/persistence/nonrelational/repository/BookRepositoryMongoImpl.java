package edu.eci.dosw.tdd.persistence.nonrelational.repository;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.port.IBookRepository;
import edu.eci.dosw.tdd.persistence.nonrelational.mapper.BookDocumentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("mongo")
@RequiredArgsConstructor
public class BookRepositoryMongoImpl implements IBookRepository {

    private final MongoBookRepository mongoBookRepository;
    private final BookDocumentMapper bookDocumentMapper;

    @Override
    public Book save(Book book) {
        return bookDocumentMapper.toDomain(
                mongoBookRepository.save(bookDocumentMapper.toDocument(book)));
    }

    @Override
    public Optional<Book> findById(String id) {
        return mongoBookRepository.findById(id).map(bookDocumentMapper::toDomain);
    }

    @Override
    public List<Book> findAll() {
        return mongoBookRepository.findAll()
                .stream()
                .map(bookDocumentMapper::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        mongoBookRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return mongoBookRepository.existsById(id);
    }
}

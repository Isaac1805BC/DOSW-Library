package edu.eci.dosw.tdd.persistence.relational.repository;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.port.IBookRepository;
import edu.eci.dosw.tdd.persistence.relational.mapper.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("relational")
@RequiredArgsConstructor
public class BookRepositoryJpaImpl implements IBookRepository {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public Book save(Book book) {
        return bookMapper.toModel(bookRepository.save(bookMapper.toEntity(book)));
    }

    @Override
    public Optional<Book> findById(String id) {
        return bookRepository.findById(id).map(bookMapper::toModel);
    }

    @Override
    public List<Book> findAll() {
        return bookMapper.toModelList(bookRepository.findAll());
    }

    @Override
    public void deleteById(String id) {
        bookRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return bookRepository.existsById(id);
    }
}

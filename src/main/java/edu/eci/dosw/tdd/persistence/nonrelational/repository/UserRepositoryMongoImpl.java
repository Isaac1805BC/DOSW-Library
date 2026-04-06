package edu.eci.dosw.tdd.persistence.nonrelational.repository;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.port.IUserRepository;
import edu.eci.dosw.tdd.persistence.nonrelational.mapper.UserDocumentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Profile("mongo")
@RequiredArgsConstructor
public class UserRepositoryMongoImpl implements IUserRepository {

    private final MongoUserRepository mongoUserRepository;
    private final UserDocumentMapper userDocumentMapper;

    @Override
    public User save(User user) {
        return userDocumentMapper.toDomain(
                mongoUserRepository.save(userDocumentMapper.toDocument(user)));
    }

    @Override
    public Optional<User> findById(String id) {
        return mongoUserRepository.findById(id).map(userDocumentMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return mongoUserRepository.findAll()
                .stream()
                .map(userDocumentMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(String id) {
        return mongoUserRepository.existsById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return mongoUserRepository.findByEmail(email).map(userDocumentMapper::toDomain);
    }
}

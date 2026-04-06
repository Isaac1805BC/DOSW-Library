package edu.eci.dosw.tdd.persistence.relational.repository;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.port.IUserRepository;
import edu.eci.dosw.tdd.persistence.relational.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("relational")
@RequiredArgsConstructor
public class UserRepositoryJpaImpl implements IUserRepository {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User save(User user) {
        return userMapper.toModel(userRepository.save(userMapper.toEntity(user)));
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id).map(userMapper::toModel);
    }

    @Override
    public List<User> findAll() {
        return userMapper.toModelList(userRepository.findAll());
    }

    @Override
    public boolean existsById(String id) {
        return userRepository.existsById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::toModel);
    }
}

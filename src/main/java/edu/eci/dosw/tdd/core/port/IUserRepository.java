package edu.eci.dosw.tdd.core.port;

import edu.eci.dosw.tdd.core.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {
    User save(User user);
    Optional<User> findById(String id);
    List<User> findAll();
    boolean existsById(String id);
    Optional<User> findByEmail(String email);
}

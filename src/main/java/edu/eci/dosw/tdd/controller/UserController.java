package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.controller.dto.UserRequest;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.service.LibraryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final LibraryService libraryService;

    @GetMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public List<User> getAllUsers() {
        return libraryService.getAllUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public User getUserById(@PathVariable String id) {
        return libraryService.getUserById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('LIBRARIAN')")
    public void registerUser(@Valid @RequestBody UserRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setRole(request.role());
        user.setMembershipType(request.membershipType());
        user.setRegisteredAt(LocalDate.now());
        libraryService.registerUser(user);
    }
}

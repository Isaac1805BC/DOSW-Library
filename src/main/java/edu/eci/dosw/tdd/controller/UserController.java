package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.service.LibraryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final LibraryService libraryService;

    public UserController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

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
    @PreAuthorize("hasRole('LIBRARIAN')")
    public void registerUser(@RequestBody User user) {
        libraryService.registerUser(user);
    }
}

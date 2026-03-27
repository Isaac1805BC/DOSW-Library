package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.service.LibraryService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('LIBRARIAN')")
    public void registerUser(@RequestBody User user) {
        libraryService.registerUser(user);
    }
}

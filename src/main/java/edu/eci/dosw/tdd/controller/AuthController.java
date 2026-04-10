package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.controller.dto.UserRequest;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.port.IUserRepository;
import edu.eci.dosw.tdd.core.service.LibraryService;
import edu.eci.dosw.tdd.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final IUserRepository userRepository;
    private final LibraryService libraryService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.get("username"),
                            credentials.get("password")
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow();

            String role = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(r -> r.replace("ROLE_", ""))
                    .collect(Collectors.joining(","));

            String token = jwtUtil.generateToken(userDetails, user.getId(), role);

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "userId", user.getId(),
                    "role", role
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody UserRequest request) {
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

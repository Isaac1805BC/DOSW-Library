package edu.eci.dosw.tdd.controller.dto;

import edu.eci.dosw.tdd.core.model.MembershipType;
import edu.eci.dosw.tdd.core.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String password,
        @NotNull Role role,
        MembershipType membershipType
) {}

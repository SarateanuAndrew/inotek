package org.example.exportproback.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.exportproback.auth.domain.Role;

@Data
public class RegisterRequest {
    @NotBlank @Email
    private String email;
    @NotBlank @Size(min = 8)
    private String password;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private Role role = Role.PRODUCER;
}

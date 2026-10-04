package org.example.exportproback.auth.dto;

import lombok.Builder;
import lombok.Data;
import org.example.exportproback.auth.domain.Role;
import java.util.UUID;

@Data
@Builder
public class AuthResponse {
    private String token;
    private UUID userId;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
}

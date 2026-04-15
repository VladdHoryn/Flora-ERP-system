package org.example.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.domain.user.UserRole;

@Getter
@Setter
public class CreateUserRequest {
    private String name;
    private String password;
    private UserRole role;
}

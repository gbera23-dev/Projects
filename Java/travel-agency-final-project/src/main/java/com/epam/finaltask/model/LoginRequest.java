package com.epam.finaltask.model;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "username not provided")
    @Size(min = 3, max = 20, message = "username must not be too large or too small")
    private String username;
    @NotBlank(message = "password not provided")
    @Size(min = 8, message = "password must be at least eight characters")
    private String password;
}

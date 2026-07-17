package com.epam.finaltask.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {
    @NotBlank(message = "username not provided")
    @Size(min = 3, max = 20, message = "username must not be too large or too small")
    private String username;
    @NotBlank(message = "password not provided")
    @Size(min = 8, message = "password must be at least eight characters")
    private String password;
    @NotBlank(message = "repeated password not provided")
    @Size(min = 8, message = "password must be at least eight characters")
    private String repeatedPassword;
    @NotBlank(message = "role is not provided")
    private String role;
}

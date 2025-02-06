package com.example.edvantistask.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(
        @NotBlank(message = "Username is required") String username,
        @Size(min = 6, message = "Password must be at least 6 characters long") @NotBlank(message = "Password is required") String password
) {}

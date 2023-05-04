package com.example.graphql.dto.request;

import com.example.graphql.model.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        Long id,
        @NotEmpty()
        @Size(min=3 , max = 20)
        String userName,
        @Email
        @NotEmpty()
        String mail,
        Role role
) { }

package com.example.graphql.dto.response;

import com.example.graphql.model.Role;

import java.time.OffsetDateTime;

public record UserDto(
        Long id,
        String userName,
        String mail,
        Role role,
        OffsetDateTime created,
        OffsetDateTime updated
) { }

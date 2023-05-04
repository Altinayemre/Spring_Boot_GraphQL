package com.example.graphql.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "`user`")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity{
    private String userName;
    private String mail;
    @Enumerated(EnumType.STRING)
    private Role role;
}

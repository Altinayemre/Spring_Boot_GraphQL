package com.example.graphql.controller;

import com.example.graphql.dto.request.UserCreateRequest;
import com.example.graphql.dto.request.UserUpdateRequest;
import com.example.graphql.dto.response.UserDto;
import com.example.graphql.service.UserService;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @QueryMapping
    List<UserDto> getAllUsers(){
        return userService.getAllUsers();
    }

    @QueryMapping()
    Optional<UserDto> getUserById(@Argument Long id){
        return userService.getUserById(id);
    }

    @MutationMapping
    UserDto createUser(@Argument @Valid UserCreateRequest userCreateRequest){
        return userService.createUser(userCreateRequest);
    }

    @MutationMapping
    UserDto updateUser(@Argument @Valid UserUpdateRequest userUpdateRequest){
        return userService.updateUser(userUpdateRequest);
    }

    @MutationMapping
    Boolean deleteUser(@Argument Long id){
        this.userService.deleteUser(id);
        return true;
    }
}

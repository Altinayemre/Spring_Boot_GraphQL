package com.example.graphql.service;

import com.example.graphql.dto.converter.UserDtoConverter;
import com.example.graphql.dto.request.UserCreateRequest;
import com.example.graphql.dto.request.UserUpdateRequest;
import com.example.graphql.dto.response.UserDto;
import com.example.graphql.exception.UserNotFoundException;
import com.example.graphql.model.User;
import com.example.graphql.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserDtoConverter userDtoConverter;

    public UserService(UserRepository userRepository,
                       UserDtoConverter userDtoConverter) {
        this.userRepository = userRepository;
        this.userDtoConverter = userDtoConverter;
    }

    protected User findUserById(Long id){
        return this.userRepository.findById(id).orElseThrow(
                ()->new UserNotFoundException("User not found!")
        );
    }

    public List<UserDto> getAllUsers(){
        return this.userRepository.findAll()
                .stream()
                .map(userDtoConverter::convert)
                .collect(Collectors.toList());
    }

    public Optional<UserDto> getUserById(Long id){
        return Optional.ofNullable(this.userDtoConverter.convert(findUserById(id)));
    }

    public UserDto createUser(UserCreateRequest userCreateRequest) {
        User user = new User(
                userCreateRequest.userName(),
                userCreateRequest.mail(),
                userCreateRequest.role());

        return this.userDtoConverter.convert(this.userRepository.save(user));
    }

    public UserDto updateUser(UserUpdateRequest userUpdateRequest) {
        User existsUser = findUserById(userUpdateRequest.id());
        existsUser.setUserName(userUpdateRequest.userName());
        existsUser.setMail(userUpdateRequest.mail());
        existsUser.setRole(userUpdateRequest.role());

        return this.userDtoConverter.convert(this.userRepository.save(existsUser));
    }

    public void deleteUser(Long id) {
        User existsUser = findUserById(id);
        this.userRepository.delete(existsUser);
    }
}

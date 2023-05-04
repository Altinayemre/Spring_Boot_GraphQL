package com.example.graphql.dto.converter;

import com.example.graphql.dto.response.UserDto;
import com.example.graphql.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserDtoConverter {

    public UserDto convert(User user){
        return new UserDto(
                user.getId(),
                user.getUserName(),
                user.getMail(),
                user.getRole(),
                user.getCreated(),
                user.getUpdated()
        );
    }

}

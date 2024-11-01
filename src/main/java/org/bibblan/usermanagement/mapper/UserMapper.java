package org.bibblan.usermanagement.mapper;

import org.bibblan.usermanagement.dto.UserDTO;
import org.bibblan.usermanagement.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user){
        if(user == null){
            return new UserDTO();
        }
        return UserDTO.builder()
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(null)
                .build();
    }

    public User toEntity(UserDTO userDTO){
        if(userDTO == null){
            return new User();
        }
        return User.builder()
                .name(userDTO.getName())
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(null)
                .build();
    }
}

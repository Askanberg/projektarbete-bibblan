package org.bibblan.usermanagement.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.bibblan.usermanagement.dto.UserDTO;
import org.bibblan.usermanagement.exception.UserAlreadyExistsException;
import org.bibblan.usermanagement.mapper.UserMapper;
import org.bibblan.usermanagement.user.User;
import org.bibblan.usermanagement.userrepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Validated
@Service
public class UserService {

    final
    UserMapper userMapper;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserMapper userMapper, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public UserDTO getUserDTOByUsername(String username) {
        Optional<User> u = userRepository.findByUsername(username);
        UserDTO userDTO;
        if(u.isPresent()){
             return userMapper.toDTO(u.get());
        }
        return null;
    }

    public UserDTO getUserDTOById(Integer ID) {
        Optional<User> u = userRepository.findById(ID);
        return u.map(userMapper::toDTO).orElse(null);
    }

    @Transactional
    public User registerNewUser( @Valid UserDTO userDTO) {

        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()){
            throw new UserAlreadyExistsException("User already exists.");
        }
        User u = User.builder()
                .name(userDTO.getName())
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password((passwordEncoder.encode(userDTO.getPassword())))
                .build();

        return userRepository.save(u);
    }


}

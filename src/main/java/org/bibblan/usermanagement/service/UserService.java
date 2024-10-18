package org.bibblan.usermanagement.service;

import jakarta.transaction.Transactional;
import org.bibblan.usermanagement.dto.UserDTO;
import org.bibblan.usermanagement.exception.UserAlreadyExistsException;
import org.bibblan.usermanagement.mapper.UserMapper;
import org.bibblan.usermanagement.user.User;
import org.bibblan.usermanagement.userrepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public UserDTO getUserDTOByUsername(String username) {
        Optional<User> u = userRepository.findByUsername(username);
        return u.map(user -> userMapper.toDTO(user)).orElse(null);
    }

    public UserDTO getUserDTOById(Integer ID) {
        Optional<User> u = userRepository.findById(ID);
        return u.map(user -> userMapper.toDTO(user)).orElse(null);
    }

    @Transactional
    public User registerNewUser(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent())
            throw new UserAlreadyExistsException("User already exists.");

        User u = User.builder()
                .name(userDTO.getName())
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password((passwordEncoder.encode(userDTO.getPassword())))
                .build();

        return userRepository.save(u);
    }


}

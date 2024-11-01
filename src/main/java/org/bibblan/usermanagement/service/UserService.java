package org.bibblan.usermanagement.service;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.bibblan.usermanagement.dto.UserDTO;
import org.bibblan.usermanagement.exception.InvalidUserInputException;
import org.bibblan.usermanagement.exception.UserAlreadyExistsException;
import org.bibblan.usermanagement.exception.UserNotFoundException;
import org.bibblan.usermanagement.mapper.UserMapper;
import org.bibblan.usermanagement.user.User;
import org.bibblan.usermanagement.userrepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final Validator validator;

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserMapper userMapper, UserRepository userRepository, PasswordEncoder passwordEncoder,  Validator validator) {
        this.validator = validator;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getUsers() {
        List<User> users = userRepository.findAll();
        if(users.isEmpty()){
            throw new UserNotFoundException("No registered users yet.");
        }
        return users;
    }

    public UserDTO getUserDTOByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new UserNotFoundException("No registered user with that username."));
    }

    public UserDTO getUserDTOById(Integer id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new UserNotFoundException("No registered user with that ID."));
    }



    @Transactional
    public User registerNewUser(UserDTO userDTO) {
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        if(!violations.isEmpty()){
            String[] errorMessage = new String[1];
            violations.forEach(constraintViolation -> errorMessage[0] = constraintViolation.getMessage());
            throw new InvalidUserInputException(errorMessage[0], violations);
        }
        if(userRepository.findByUsername(userDTO.getUsername()).isPresent()){
            throw new UserAlreadyExistsException("User already exists.");
        }
        User u = User.builder().name(userDTO.getName()).username(userDTO.getUsername()).email(userDTO.getEmail()).password((passwordEncoder.encode(userDTO.getPassword()))).build();
        System.out.println(u);
        u = userRepository.save(u);
        System.out.println(u);
        return u;
    }


}

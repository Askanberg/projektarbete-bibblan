package org.bibblan.usermanagement.service;

import jakarta.transaction.Transactional;
import org.bibblan.usermanagement.security.UserSecurity;
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
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder = UserSecurity.passwordEncoder();

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserById(Integer ID) {
        return userRepository.findById(ID);
    }

    @Transactional
    public User registerNewUser(String name, String username, String password, String email) {
        throwExceptionIfUserExists(userRepository.findByUsername(username).isPresent());
        User u = User.builder().name(name).username(username).password(passwordEncoder.encode(password)).email(email).build();
        return userRepository.save(u);
    }

    public void throwExceptionIfUserExists(boolean condition) {
        if (condition) {
            throw new IllegalArgumentException("User already exists.");
        }
    }


}

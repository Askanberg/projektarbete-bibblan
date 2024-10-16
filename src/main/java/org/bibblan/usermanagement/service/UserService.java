package org.bibblan.usermanagement.service;

import org.bibblan.usermanagement.security.SecurityConfig;
import org.bibblan.usermanagement.user.User;
import org.bibblan.usermanagement.userrepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder = SecurityConfig.passwordEncoder();

    @PostMapping(path="/register")
    public @ResponseBody User registerNewUser(@RequestParam String name, @RequestParam String userName, @RequestParam String password, @RequestParam String email){

        String encodePassword = passwordEncoder.encode(password);

        return User.builder()
                .name(name)
                .username(userName)
                .password(encodePassword)
                .email(email)
                .build();
    }

    @GetMapping("/userDemo/getUser/{username}")
    public @ResponseBody ResponseEntity<User> getUser(@PathVariable String username){
        Optional<User> u = userRepository.findByUsername(username);
        return u.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}

package org.bibblan.usermanagement.service;

import org.bibblan.usermanagement.user.User;
import org.bibblan.usermanagement.userrepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(path="/register")
    public @ResponseBody User registerNewUser(@RequestParam String name, @RequestParam String userName, @RequestParam String password, @RequestParam String email){

        String encodePassword = passwordEncoder.encode(password);

        User u = User.builder()
                .name(name)
                .userName(userName)
                .password(encodePassword)
                .email(email)
                .build();

        return u;
    }

}

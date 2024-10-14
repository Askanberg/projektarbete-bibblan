package org.bibblan.usermanagement.controller;

import org.bibblan.usermanagement.user.User;
import org.bibblan.usermanagement.userrepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/userDemo")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping(path="/register")
    public @ResponseBody String addNewUser(@RequestParam String name, @RequestParam String userName, @RequestParam String password, @RequestParam String email){

        User u = User.builder()
                .name(name)
                .userName(userName)
                .password(password)
                .email(email)
                .build();

        return "User saved.";
    }
    @GetMapping(path="/allUsers")
    public @ResponseBody Iterable<User> getUsers(){

        return userRepository.findAll();
    }
}

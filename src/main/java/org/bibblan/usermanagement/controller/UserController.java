package org.bibblan.usermanagement.controller;

import lombok.Data;
import org.bibblan.usermanagement.user.User;
import org.bibblan.usermanagement.userrepository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Data
@Controller
@RequestMapping(path="/userDemo")
public class UserController {

    private UserRepository userRepository;

    @GetMapping(path="/allUsers")
    public @ResponseBody Iterable<User> getUsers(){

        return userRepository.findAll();
    }

    @GetMapping(path="/getUser")
    public @ResponseBody User getUser(){
        return User.builder().build();
    }
}

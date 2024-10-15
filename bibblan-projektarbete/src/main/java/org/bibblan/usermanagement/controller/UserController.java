package org.bibblan.usermanagement.controller;

import org.bibblan.usermanagement.user.User;
import org.bibblan.usermanagement.userrepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/userDemo")
public class UserController {

    private UserRepository userRepository;




    @GetMapping(path="/allUsers")
    public @ResponseBody Iterable<User> getUsers(){

        return userRepository.findAll();
    }
}

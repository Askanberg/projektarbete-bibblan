package org.bibblan.usermanagement.controller;

import jakarta.validation.Valid;
import lombok.Data;
import org.bibblan.usermanagement.service.UserService;
import org.bibblan.usermanagement.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Data
@Controller
@RequestMapping(path="/userDemo")
public class UserController {

    @Autowired
    private UserService userService;



    @GetMapping(path="/allUsers")
    public @ResponseBody Iterable<User> getUsers(){
        return userService.getUsers();
    }

    @PostMapping(path="/register")
    public @ResponseBody String registerUser(@RequestParam String name, @RequestParam String username, @RequestParam String password, @RequestParam String email){
        userService.registerNewUser(name, username, password, email);
        return "User successfully registered.";
    }

    @GetMapping("/userDemo/getUser/{username}")
    public @ResponseBody ResponseEntity<User> getUser(@PathVariable @Valid String username){
        Optional<User> u = userService.getUserByUsername(username);
        return u.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

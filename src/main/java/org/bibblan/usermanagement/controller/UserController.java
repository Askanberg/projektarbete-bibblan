package org.bibblan.usermanagement.controller;

import jakarta.persistence.GeneratedValue;
import jakarta.validation.Valid;
import lombok.Data;
import org.apache.coyote.Response;
import org.bibblan.usermanagement.exception.UserNotFoundException;
import org.bibblan.usermanagement.service.UserService;
import org.bibblan.usermanagement.user.User;
import org.bibblan.usermanagement.userrepository.UserRepository;
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
    @Autowired
    private UserRepository userRepository;

    @GetMapping(path="/allUsers")
    public @ResponseBody Iterable<User> getUsers(){
        return userService.getUsers();
    }

    @PostMapping(path="/register")
    public @ResponseBody ResponseEntity<String> registerUser(@RequestParam String name, @RequestParam String username, @RequestParam String password, @RequestParam String email){
        Optional<User> u = userRepository.findByUsername(username);
        if(u.isPresent()){
            return ResponseEntity.badRequest().build();
        }
        userService.registerNewUser(name, username, password, email);
        return ResponseEntity.ok("User successfully registered.");
    }

    @GetMapping("/userDemo/getUser/{username}")
    public @ResponseBody ResponseEntity<User> getUserByUsername(@PathVariable @Valid String username){
        Optional<User> u = userService.getUserByUsername(username);
        return u.map(ResponseEntity::ok).orElseThrow(()-> new UserNotFoundException("That ID does not match any user."));
    }

    @GetMapping(path = "/userDemo/getUser/{id}")
    public @ResponseBody ResponseEntity<User> getUserById(@PathVariable @Valid Integer id){
        Optional<User> u = userService.getUserById(id);
        return u.map(ResponseEntity::ok).orElseThrow(()-> new UserNotFoundException("The username does not match any user."));
    }

}

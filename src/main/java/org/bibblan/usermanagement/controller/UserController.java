package org.bibblan.usermanagement.controller;

import jakarta.validation.Valid;
import lombok.Data;
import org.bibblan.usermanagement.dto.UserDTO;
import org.bibblan.usermanagement.exception.UserNotFoundException;
import org.bibblan.usermanagement.mapper.UserMapper;
import org.bibblan.usermanagement.service.UserService;
import org.bibblan.usermanagement.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Data
@RestController
@RequestMapping(path="/userDemo")
@Validated
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping(path="/allUsers")
    public Iterable<User> getUsers(){
        return userService.getUsers();
    }

    @PostMapping(path="/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDTO userDTO){
        System.out.println(ResponseEntity.ok().build());
        System.out.println("Received UserDTO: " + userDTO);
        userService.registerNewUser(userDTO);
        return ResponseEntity.ok("User successfully registered.");
    }

    @GetMapping("/getUser/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable @Valid String username){
        UserDTO u = userService.getUserDTOByUsername(username);
        if(u == null){
            throw new UserNotFoundException("No registered user with that username.");
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/getUser/{id}")
    public ResponseEntity<User> getUserById(@PathVariable @Valid Integer id){
        UserDTO u = userService.getUserDTOById(id);
        return ResponseEntity.ok().build();
    }

}

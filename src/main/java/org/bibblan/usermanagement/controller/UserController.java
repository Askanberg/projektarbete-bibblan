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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Data
@Controller
@RequestMapping(path="/userDemo")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    UserMapper userMapper;

    @GetMapping(path="/allUsers")
    public @ResponseBody Iterable<User> getUsers(){
        return userService.getUsers();
    }

    @PostMapping(path="/register")
    public @ResponseBody ResponseEntity<String> registerUser(@Valid @RequestBody UserDTO userDTO){
        UserDTO temp = userService.getUserDTOByUsername(userDTO.getUsername());

        if(temp != null){
            return ResponseEntity.badRequest().build();
        }
        userService.registerNewUser(userDTO);
        return ResponseEntity.ok("User successfully registered.");
    }

    @GetMapping("/userDemo/getUser/{username}")
    public @ResponseBody ResponseEntity<User> getUserByUsername(@PathVariable @Valid String username){
        UserDTO u = userService.getUserDTOByUsername(username);
        if(u == null){
            throw new UserNotFoundException("User ");
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/userDemo/getUser/{id}")
    public @ResponseBody ResponseEntity<User> getUserById(@PathVariable @Valid Integer id){
        UserDTO u = userService.getUserDTOById(id);
        return ResponseEntity.ok().build();
    }

}

package org.bibblan.usermanagement.controller;

import jakarta.validation.Valid;
import lombok.Data;
import org.bibblan.usermanagement.dto.UserDto;
import org.bibblan.usermanagement.exception.UserNotFoundException;
import org.bibblan.usermanagement.service.UserService;
import org.bibblan.usermanagement.user.User;
import org.bibblan.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.security.Principal;

@Data
@RestController
@Validated
@RequestMapping(path="/api/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerNewUser(@Valid @RequestBody UserDto userDto) {
        userService.registerNewUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).location(URI.create("/profile")).body("User successfully registered.");
    }
    @PostMapping("/admin/delete")
    public ResponseEntity<?> delete(@RequestBody UserDto userDto) throws AccessDeniedException {
        userService.deleteUser(userDto.getEmail());
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/home")).build();

    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestParam String email, @RequestParam String password) {
        UserDto userDto = userService.getUserDTOByEmail(email);
        User user;
        if (userDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if(userRepository.findByEmail(email).isPresent()){
            user = userRepository.findByEmail(email).get();
            if(!password.matches(user.getPassword())){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
        return ResponseEntity.ok(userDto);
    }

    @GetMapping(path = "/profile")
    public ResponseEntity<UserDto> getUserProfile(Principal principal) {
        if (principal instanceof OAuth2AuthenticationToken authToken) {
            String email = authToken.getPrincipal().getAttribute("email");

            UserDto user = userService.getUserDTOByEmail(email);

            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping(path="/allUsers")
    public ResponseEntity<Iterable<User>> getUsers(){
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/getUser/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable @Valid String username){
        UserDto u = userService.getUserDTOByUsername(username);
        if(u == null){
            throw new UserNotFoundException("No registered user with that username.");
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/getUser/id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable @Valid Integer id){
        UserDto u = userService.getUserDTOById(id);
        if(u == null) {
            throw new UserNotFoundException("No registered user with that id.");
        }
        return ResponseEntity.ok().build();
    }

}


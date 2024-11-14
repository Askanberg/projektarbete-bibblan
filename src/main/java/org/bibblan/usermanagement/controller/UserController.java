package org.bibblan.usermanagement.controller;

import jakarta.validation.Valid;
import lombok.Data;
import org.bibblan.usermanagement.dto.UserDto;
import org.bibblan.usermanagement.exception.UserNotFoundException;
import org.bibblan.usermanagement.role.Role;
import org.bibblan.usermanagement.service.UserService;
import org.bibblan.usermanagement.user.User;
import org.bibblan.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@RestController
@Validated
@RequestMapping(path="/api/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerNewUser(@Valid @RequestBody UserDto userDto) {
        User u = userService.registerNewUser(userDto);



        return ResponseEntity.status(HttpStatus.OK).location(URI.create("/profile")).body("User successfully registered.");
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        UserDto userDto = userService.getUserDTOByEmail(email);
        if (userDto != null && userService.authenticateUser(email, password)) {

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDto.getEmail(),
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            authentication.setAuthenticated(true);

            return ResponseEntity.status(HttpStatus.OK).location(URI.create("/api/users/local/profile")).body(userDto);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping(path = "/logout")
    public ResponseEntity<?> logout(@RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/home"))
                .body(userDto);
    }


    @GetMapping(path = "/local/profile")
    public ResponseEntity<UserDto> getLocalUserProfile(){
        return ResponseEntity.ok().build();
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

    @GetMapping(path="/allUsers")
    public ResponseEntity<Iterable<User>> getUsers(){
        return ResponseEntity.ok(userService.getUsers());
    }

}


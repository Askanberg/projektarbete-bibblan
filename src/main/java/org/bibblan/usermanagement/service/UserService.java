package org.bibblan.usermanagement.service;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.bibblan.usermanagement.dto.UserDto;
import org.bibblan.usermanagement.exception.InvalidUserInputException;
import org.bibblan.usermanagement.exception.UserAlreadyExistsException;
import org.bibblan.usermanagement.exception.UserNotFoundException;
import org.bibblan.usermanagement.mapper.UserMapper;
import org.bibblan.usermanagement.role.Role;
import org.bibblan.usermanagement.repository.RoleRepository;
import org.bibblan.usermanagement.user.User;
import org.bibblan.usermanagement.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService extends DefaultOAuth2UserService {

    private final RoleRepository roleRepository;
    private final Validator validator;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registerNewUser(@Valid UserDto userDto) {
        validateUserDTO(userDto);
        checkUsernameAvailability(userDto);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDto.getEmail(),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User u = new User(userDto.getEmail(),userDto.getName(), userDto.getUsername(), passwordEncoder.encode(userDto.getPassword()));
        return userRepository.save(addDefaultRoleToUser(u));
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");

        if (email != null && userRepository.findByEmail(email).isEmpty()) {
            User newUser = new User(email, oAuth2User.getName(), oAuth2User.getName(), passwordEncoder.encode(UUID.randomUUID().toString()));
            newUser.getRoles().add(getOrCreateDefaultRole());
            userRepository.save(newUser);
        }
        return oAuth2User;
    }

    public List<User> getUsers() {
        List<User> users = userRepository.findAll();
        if(users.isEmpty()){
            throw new UserNotFoundException("No registered users yet.");
        }
        return users;
    }

    public UserDto getUserDTOByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserMapper::toDTO)
                .orElseThrow(() -> new UserNotFoundException("No registered user with that username."));
    }

    public UserDto getUserDTOById(Integer id) {
        return userRepository.findById(id)
                .map(UserMapper::toDTO)
                .orElseThrow(() -> new UserNotFoundException("No registered user with that ID."));
    }

    public UserDto getUserDTOByEmail(String email){
        Optional<User> u = userRepository.findByEmail(email);
        if(u.isPresent()){
            return UserMapper.toDTO(u.get());
        }
        throw new UserNotFoundException("No registered user with that email.");
    }

    public boolean authenticateUser(String email, String password){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.filter(value -> passwordEncoder.matches(password, value.getPassword())).isPresent()){

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    optionalUser.get().getEmail(),
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );
            return true;
        }
        return false;

    }

    private void validateUserDTO(UserDto userDTO){
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDTO);
        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            throw new InvalidUserInputException(errorMessage, violations);
        }
    }

    private void checkUsernameAvailability(UserDto userDTO) {
        if(userRepository.findByUsername(userDTO.getUsername()).isPresent()){
            throw new UserAlreadyExistsException("A user is already registered with that username.");
        }
    }

    private User addDefaultRoleToUser(User u) {
        u.getRoles().add(getOrCreateDefaultRole());
        return u;
    }

    private Role getOrCreateDefaultRole() {
        return roleRepository.findByName("ROLE_MEMBER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_MEMBER")));
    }
}

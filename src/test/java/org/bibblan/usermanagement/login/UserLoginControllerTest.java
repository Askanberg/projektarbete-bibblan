package org.bibblan.usermanagement.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bibblan.Main;
import org.bibblan.core.util.SecurityConfig;
import org.bibblan.usermanagement.controller.UserController;
import org.bibblan.usermanagement.dto.UserDto;
import org.bibblan.usermanagement.repository.UserRepository;
import org.bibblan.usermanagement.role.Role;
import org.bibblan.usermanagement.service.CustomOidcUserService;
import org.bibblan.usermanagement.service.UserService;
import org.bibblan.usermanagement.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = {Main.class, SecurityConfig.class})
public class UserLoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @MockBean
    CustomOidcUserService customOidcUserService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    UserService userService;

    void setupMockUser(){
        when(userRepository.save(any(User.class))).thenAnswer(a -> {
            User mockUser = a.getArgument(0);
            mockUser.setPassword("password123123");
            mockUser.setName("name");
            mockUser.setUsername("username");
            mockUser.setEmail("some@gmail.com");
            mockUser.getRoles().add(new Role("ROLE_MEMBER"));
            mockUser.setID(1);
            Authentication auth = new TestingAuthenticationToken(mockUser, null, "ROLE_MEMBER");
            SecurityContextHolder.getContext().setAuthentication(auth);
            return mockUser;
        });
    }

    void navigateToLoginForm() throws Exception {

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/home.html"));

        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/login.html"));

    }

    @DisplayName("Loggar in med lokalt konto fungerar trots felinmatning.")
    @WithMockUser
    @Test
    public void loginWIthValidCredentialsForLocalAccount() throws Exception {
        navigateToLoginForm();

        setupMockUser();

        UserDto invalidUserDto = new UserDto("some@email.com", "username", "name", "");
        invalidUserDto.getRoles().add(new Role("ROLE_MEMBER"));

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper.writeValueAsString(invalidUserDto)))
                .andExpect(status().isBadRequest());

        User user = userService.registerNewUser(new UserDto());
        String userJson = jacksonObjectMapper.writeValueAsString(user);
        String jsonCredentials = "{ email\": \"some@gmail.com\", \"password\": \"password123123 }";

        mockMvc.perform(post(("/api/users/login"))
                        .contentType(MediaType.APPLICATION_JSON).content(jsonCredentials))
                .andExpect(forwardedUrl("/profile"))
                .andExpect(status().isOk());
    }
}
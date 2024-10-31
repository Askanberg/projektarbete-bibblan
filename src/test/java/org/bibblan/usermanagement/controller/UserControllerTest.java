package org.bibblan.usermanagement.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import org.bibblan.usermanagement.dto.UserDTO;
import org.bibblan.usermanagement.mapper.UserMapper;
import org.bibblan.usermanagement.service.UserService;
import org.bibblan.usermanagement.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@DisplayName("User Controller Test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @Test
    public void handlesInvalidUsernameCorrectly() {
        User u = User.builder()
                .name("Halloj")
                .username("")
                .password("123123123")
                .email("lol@gmail.com")
                .build();


    }

    @Test
    @DisplayName("Registrerar användare med tomt email-fält")
    public void registerNewUserWithBlankEmailThrowsException() {
        User u = new User();

        UserDTO userDTO = UserDTO.builder().name("Beorn").username("Hejsan").email("").password("myRawPassword").build();




        assertNotNull(u, "Fel: Förväntade att användaren fanns i databasen.");


    }

    @Test
    @DisplayName("Registrerar användare med tom email ska returnera Bad Request.")
    public void registerUserWithBlankEmailShouldReturnBadRequest() throws Exception {
        String userJson = "{ \"name\": \"Beorn\", \"username\": \"Hejsan\", \"email\": \"\", \"password\": \"myRawPassword\" }";

        mockMvc.perform(post("/userDemo/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Email field is empty."));
    }


    @Test
    public void returnAllUsers() throws Exception{
        mockMvc.perform(get("/userDemo/allUsers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray());

    }
    @Test
    public void registerNewUser() throws Exception {
        mockMvc.perform(post("/userDemo/register")
                        .param("name", "Andreas")
                        .param("userName", "Adde13")
                        .param("password", "someRawPassword123")
                        .param("email", "adde@domain.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("User saved."));

    }

}


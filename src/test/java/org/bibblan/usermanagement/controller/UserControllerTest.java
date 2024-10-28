package org.bibblan.usermanagement.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.bibblan.usermanagement.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@SpringBootTest
@DisplayName("User Controller Test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void handlesInvalidUsernameCorrectly() {
        User u = User.builder()
                .name("Halloj")
                .username("")
                .password("123123123")
                .email("lol@gmail.com")
                .build();

//        mockMvc.perform(get("/userDemo/register"))

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


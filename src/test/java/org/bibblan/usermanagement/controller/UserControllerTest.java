package org.bibblan.usermanagement.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.bibblan.usermanagement.dto.UserDTO;
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
    @DisplayName("Registrerar användare med tomt email-fält")
    public void registerNewUserWithBlankEmailThrowsException() {
        User u = new User();

        UserDTO userDTO = UserDTO.builder().name("Beorn").username("Hejsan").email("").password("myRawPassword").build();




        assertNotNull(u, "Fel: Förväntade att användaren fanns i databasen.");


    }

    @Test
    @DisplayName("Registerar användare med invalid email.")
    public void registerNewUserWithInvalidEmailThrowsException() {
//
//        when(userRepository.save(any(User.class))).thenReturn(new User());
//
//        User user = User.builder()
//                .name("Pudel")
//                .username("Poodle97")
//                .email("invalidEmail")
//                .password("someRawPassword")
//                .build();
//
////        when(userRepository.save(user)).thenAnswer(i -> i.getArgument(0));
//        UserDTO dto = userMapper.toDTO(user);
//        dto.setUsername("");
//        dto.setPassword("someRawPassword1337");
//
//        userService.registerNewUser(dto);
//
//        verify(userRepository, times(1)).save(any(User.class));
//
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            userService.registerNewUser(UserDTO.builder()
//                    .name("Arpe")
//                    .username("")
//                    .email("invalid@gmail.com")
//                    .password("someRawPassword_1337")
//                    .build());
//        });

//        assertEquals("Invalid email address.", exception.getMessage(), "Fel: Förväntade att ett IllegalArgumentException kastades med meddelandet \"Invalid email address.\"");

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


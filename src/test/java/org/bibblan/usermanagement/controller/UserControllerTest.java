package org.bibblan.usermanagement.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.bibblan.usermanagement.dto.UserDTO;
import org.bibblan.usermanagement.mapper.UserMapper;
import org.bibblan.usermanagement.service.UserService;
import org.bibblan.usermanagement.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@DisplayName("User Controller Test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper userMapper;

    void saveUser(UserDTO userDTO) throws Exception {

        String userJson = "{ \"name\": \""+userDTO.getName() +"\", \"username\": \""+ userDTO.getUsername()+"\", \"email\": \""+userDTO.getEmail()+"\", \"password\": \""+userDTO.getPassword()+"\" }";

        mockMvc.perform(post("/userDemo/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().string("User successfully registered."));

    }

    private final UserDTO FIRST_DTO = UserDTO.builder()
            .name("Name")
            .username("Username")
            .email("test@email.com")
            .password("someRawPassword123123")
            .build();

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
    @DisplayName("Registrerar användare med tomt username-fält")
    public void registerNewUserWithBlankUsernameThrowsException() throws Exception {

        UserDTO userDTO = UserDTO.builder().name("Beorn").username("").email("").password("myRawPassword").build();
        String userJson = jacksonObjectMapper.writeValueAsString(userDTO);

        mockMvc.perform(post("/userDemo/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)) // Send JSON content
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username").value("Username must be between 3 and 20 characters.")); // Validate error message
    }

    @Test
    @DisplayName("Registrerar användare med tom email returnerar Bad Request.")
    public void registerUserWithBlankEmailShouldReturnBadRequest() throws Exception {
        String userJson = "{ \"name\": \"Beorn\", \"username\": \"Hejsan\", \"email\": \"\", \"password\": \"myRawPassword\" }";

        mockMvc.perform(post("/userDemo/register")
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Email field is required."));
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
        String userJson = "{ \"name\": \"Beorn\", \"username\": \"Hejsan\", \"email\": \"bunke_lunke@domain.com\", \"password\": \"myRawPassword\" }";
        mockMvc.perform(post("/userDemo/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().string("User successfully registered."));

    }

    @Test
    @DisplayName("getUserByUsername() returnerar rätt registrerade användare.")
    public void getUserByUsernameReturnsRegisteredUser() throws Exception {
        saveUser(FIRST_DTO);

        when(userService.getUserDTOByUsername("Username"))
                .thenReturn(FIRST_DTO);

        mockMvc.perform(get("/userDemo/getUser/username/{username}", "Username")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk());

        verify(userService, times(1)).getUserDTOByUsername(any(String.class));

    }

    @Test
    @DisplayName("getUserByUsername() kastar UserNotFoundException när användaren inte finns.")
    public void getUserByUsernameThrowsException() throws Exception {

        mockMvc.perform(get("/userDemo/getUser/username/{username}", "Unavailable Username")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No registered user with that username."));

    }

    @Test
    @DisplayName("getUserById() returnerar rätt registrerade användare.")
    public void getUserByIdReturnsExistingUser() throws Exception {
        UserDTO userDTO = UserDTO.builder()
                        .name("Name DTO")
                        .username("Username DTO")
                        .email("dto@email.com")
                        .password("someRawPassword1337")
                        .build();

        saveUser(userDTO);

        when(userService.getUserDTOById(1))
                .thenReturn(FIRST_DTO);

        mockMvc.perform(get("/userDemo/getUser/id/{id}", 1)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("getUserById() kastar UserNotFoundException när användaren inte finns.")
    public void getUserByIdThrowsException() throws Exception {

        mockMvc.perform(get("/userDemo/getUser/id/{id}", 1)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No registered user with that id."));

    }
}


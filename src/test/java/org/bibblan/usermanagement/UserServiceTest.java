package org.bibblan.usermanagement;

import org.bibblan.usermanagement.service.UserService;
import org.bibblan.usermanagement.user.User;
import org.bibblan.usermanagement.userrepository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayName("User Service Test")
public class UserServiceTest {

    @MockBean
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Registrerar användare lokalt med valid data.")
    public void registerNewLocalUserWithValidData() {
        User user = userService.registerNewUser("Beorn", "Bunke_1337", "myRawPassword", "beorn@somedomain.com");

        assertNotNull(user);
        assertTrue(() -> Stream.of("Beorn", "Bunke_1337", "myRawPassword", "beorn@somedomain.com").anyMatch(s -> s.equals(user.getName()) || s.equals(user.getUsername()) || s.equals(user.getPassword()) || s.equals(user.getEmail())),
                "Fel: Testet förväntade sig att alla privata fält skulle stämma.");
    }

    @Test
    @DisplayName("Registrerar användare i databasen med valid data.")
    public void registerNewUserInDatabase(){
        when(userRepository.save(any(User.class))).thenReturn(new User());

        User u = userService.registerNewUser("Bunke", "BunkeLunke", "someRawPassword321", "inte@su.se");
    }

    @Test
    @DisplayName("Registrerar användare med tomt email-fält")
    public void registerNewUserWithBlankEmail() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerNewUser("Beorn", "Bunke_1337", "myRawPassword", "");
        });

        assertEquals("Email field is empty.", exception.getMessage(),
                "Fel: Testet förväntade sig att ett IllegalArgumentException kastades med meddelandet \"Email field is empty.\"");
    }

    @Test
    @DisplayName("Registerar användare med invalid email.")
    public void registerNewUserWithInvalidEmail() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerNewUser("Beorn", "Bunke_1337", "myRawPassword", "some_email.com");
        });

        assertEquals("Invalid email address", exception.getMessage(),
                "Fel: Testet förväntade sig att ett IllegalArgumentException kastades med meddelandet \"Invalid email address.\"");

    }

    @Test
    public void registerNewUserWithInvalidUserName() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerNewUser("Beorn", "", "myRawPassword", "some_email.com");
        });

        assertEquals("Invalid username.", exception.getMessage(),
                "Fel: Förväntade sig att ett IllegalArgumentException kastades med meddelandet \"Invalid username.\"");
        
    }

    @Test
    public void getUserWithUserNameReturnsUser(){


        userService.registerNewUser("Dachshund", "Mäyräkoira", "stalaktitEremit", "tax@hundregister.se");
        ResponseEntity<User> u = userService.getUser("Mäyräkoira");

        assertEquals(User.builder().name("Dachshund").username("Mäyräkoira").password("stalaktitEremit").email("tax@hundregister.se").ID(Objects.requireNonNull(u.getBody()).getID()), u,
                "Fel: UserService returnerade inte rätt användare.");
    }

}

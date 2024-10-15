package org.bibblan.usermanagement;

import org.bibblan.usermanagement.service.UserService;
import org.bibblan.usermanagement.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Service Test")
public class UserServiceTest {
    private UserService userService = new UserService();

    @Test
    public void registerNewUserWithValidData() {
        User user = userService.registerNewUser("Beorn", "Bunke_1337", "myRawPassword", "beorn@somedomain.com");

        assertNotNull(user);
        assertTrue(() -> Stream.of("Beorn", "Bunke_1337", "myRawPassword", "beorn@somedomain.com").anyMatch(s -> s.equals(user.getName()) || s.equals(user.getUserName()) || s.equals(user.getPassword()) || s.equals(user.getEmail())),
                "Fel: Testet förväntade sig att alla privata fält skulle stämma.");
    }

    @Test
    public void registerNewUserWithBlankEmail() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerNewUser("Beorn", "Bunke_1337", "myRawPassword", "");
        });

        assertEquals("Email field is empty.", exception.getMessage(),
                "Fel: Testet förväntade sig att ett IllegalArgumentException kastades med meddelandet \"Email field is empty.\"");
    }

    @Test
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

}

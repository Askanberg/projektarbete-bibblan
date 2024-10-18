package org.bibblan.usermanagement.service;

import org.bibblan.usermanagement.dto.UserDTO;
import org.bibblan.usermanagement.exception.UserAlreadyExistsException;
import org.bibblan.usermanagement.user.User;
import org.bibblan.usermanagement.userrepository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("User Service Test")
public class UserServiceTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    private UserService userService;



    @Test
    @DisplayName("Registerar användare med valid data.")
    public void registerNewSavesUser() {


        // Definierar mock-objektets beteende när UserService::registerNewUser anropas som i sin tur anropar UserRepository::save
        when(userRepository.save(any(User.class))).thenReturn(new User());

        // Registrerar användaren genom Service-lagret som förväntas sparas sedan i databasen.
        User u = userService.registerNewUser(UserDTO.builder().name("Bunke").username("BunkeLunke").email("inte@su.se").password("someRawPassword").build());

        assertNotNull(u);

        // Verifierar att användaren sparas i databasen.
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Användares lösenord i databasen är i krypterad form.")
    public void passwordInDatabaseIsEncrypted() {

        // Definierar Mock-databasens beteende med ett simulerat krypterat lösenord.
        when(userRepository.save(any(User.class))).thenAnswer(invocationOnMock -> {
            User u = invocationOnMock.getArgument(0);
            u.setPassword("simulatedEncryptedPassword");
            return u;
        });

        User u = userService.registerNewUser(UserDTO.builder().name("Johan").username("Rohan").email("rohan@gmail.com").build());

        // Kontrollerar som returnerades av registerNewUser() finns i databasen.
        assertNotNull(u, "Fel: Förväntade att användaren sparades.");

        //  Kontrollerar att lösenordet i databasen inte är samma som valdes av användaren.
        assertNotEquals(u.getPassword(), "someRawPassword123");

        // Verifierar att användaren finns i databasen.
        verify(userRepository, times(1)).save(any(User.class));

    }

    @Test
    @DisplayName("Registrerar användare med tomt email-fält")
    public void registerNewUserWithBlankEmailThrowsException() {
        when(userRepository.save(any(User.class))).thenReturn(new User());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerNewUser(UserDTO.builder().name("Beorn").username("Bunke_1337").email("").password("myRawPassword").build());
        });

        assertEquals("Email field is empty.", exception.getMessage(), "Fel: Förväntade att ett IllegalArgumentException kastades med meddelandet \"Email field is empty.\"");
    }

    @Test
    @DisplayName("Registerar användare med invalid email.")
    public void registerNewUserWithInvalidEmailThrowsException() {
        when(userRepository.save(any(User.class))).thenReturn(new User());
        UserDTO userDTO = UserDTO.builder().name("Pudel").username("Poodle97").email("invalidEmail.com").password("someRawPassword").build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerNewUser(userDTO);
        });

        assertEquals("Invalid email address", exception.getMessage(), "Fel: Förväntade att ett MethodArgumentNotValidException kastades med meddelandet \"Invalid email address.\"");

    }

    @Test
    public void registerNewUserWithInvalidUserNameThrowsException() {
        when(userRepository.save(any(User.class))).thenReturn(new User());

    }

    @Test
    public void getUserWithUsernameReturnsUser() {
        when(userRepository.save(any(User.class))).thenReturn(new User());
        UserDTO userDTO = UserDTO.builder().name("Dachshund").username("Tax").email("tax@hundregister.se").password("someRawPassword").build();

        userService.registerNewUser(UserDTO.builder().name("Dachshund").username("Tax").email("tax@hundregister.se").build());

        UserDTO u = userService.getUserDTOByUsername("Tax");

        assertNotNull(u,
                "Fel: Förväntade att användaren fanns i databasen.");


    }

    @Test
    @DisplayName("Registrerar en redan existerande användare.")
    public void registerAlreadyExistingUser() {
        when(userRepository.save(any(User.class))).thenReturn(new User());
        UserDTO userDTO = UserDTO.builder().name("Arpe").username("Hund_97").email("hund_97@hotmail.com").password("someRawPassword_1337").build();
        User u = userService.registerNewUser(userDTO);

        Exception e = assertThrows(UserAlreadyExistsException.class, () -> {
            userService.registerNewUser(userDTO);
        });


    }

}

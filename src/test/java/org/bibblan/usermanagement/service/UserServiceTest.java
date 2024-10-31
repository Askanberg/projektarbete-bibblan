package org.bibblan.usermanagement.service;

import org.bibblan.usermanagement.dto.UserDTO;
import org.bibblan.usermanagement.exception.InvalidUserInputException;
import org.bibblan.usermanagement.exception.UserAlreadyExistsException;
import org.bibblan.usermanagement.mapper.UserMapper;
import org.bibblan.usermanagement.user.User;
import org.bibblan.usermanagement.userrepository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

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
    @Autowired
    private UserMapper userMapper;


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
            u.setPassword("fakeEncryptedPassword");
            return u;
        });

        User u = userService.registerNewUser(UserDTO.builder().name("Johan").username("Rohan").email("rohan@gmail.com").password("someRawPassword123").build());

        // Kontrollerar som returnerades av registerNewUser() finns i databasen.
        assertNotNull(u, "Fel: Förväntade att användaren sparades.");

        //  Kontrollerar att lösenordet i databasen inte är samma som valdes av användaren.
        assertNotEquals(u.getPassword(), "someRawPassword123");

        // Verifierar att användaren finns i databasen.
        verify(userRepository, times(1)).save(any(User.class));

    }

    @Test
    public void registerNewUserWithoutUsernameThrowsException() {
        when(userRepository.save(any(User.class))).thenReturn(new User());


        assertThrows(NullPointerException.class, () -> userService.registerNewUser(UserDTO.builder().name("Name").password("someRawPassword123").email("some@email.com").build()));

        verifyNoInteractions(userRepository);

    }

    @Test
    public void registerNewUserWithoutPasswordThrowsException() {
        when(userRepository.save(any(User.class))).thenReturn(new User());

        Exception e = assertThrows(InvalidUserInputException.class, () -> userService.registerNewUser(UserDTO.builder().name("Name").username("Lolipop").email("some@email.com").build()));
        System.out.println(e.getMessage());

        assertEquals("Invalid input in required fields.", e.getMessage(),
                "Felmeddelandet var inte som förväntat.");
        verifyNoInteractions(userRepository);

    }




    @Test
    public void getUserWithUsernameReturnsUser() {
        User u = User.builder().name("Some Name").username("Sven").email("someValid@email.com").password("someRawPassword").build();

        when(userRepository.save(any(User.class))).thenReturn(new User());

        UserDTO temp = userMapper.toDTO(u);
        temp.setPassword("someRawPassword");


        when(userRepository.findByUsername("Sven")).thenReturn(Optional.of(u));

        temp = userService.getUserDTOByUsername("Sven");

        assertNotNull(temp,
                "Fel: Förväntade att användaren fanns i databasen.");

        assertEquals(temp.getUsername(), u.getUsername(),
                "Fel: Förväntade att rätt användare skulle returneras");

        verify(userRepository, times(1)).findByUsername(any(String.class));

    }

    @Test
    @DisplayName("Registrerar en redan existerande användare.")
    public void registerAlreadyExistingUserThrowsException() {
        when(userRepository.save(any(User.class))).thenReturn(new User());

        User u = new User();
        when(userRepository.findByUsername("Hund_97")).thenReturn(Optional.of(u));

        Exception e = assertThrows(UserAlreadyExistsException.class, () -> userService.registerNewUser(UserDTO.builder()
                .name("Arpe")
                .username("Hund_97")
                .email("hund_97@hotmail.com")
                .password("someRawPassword_1337")
                .build()));

        assertEquals("User already exists.", e.getMessage() ,
                "Fel: Förväntade ett UserAlreadyExistsException.");

        verify(userRepository, times(1)).findByUsername(any(String.class));

    }

}

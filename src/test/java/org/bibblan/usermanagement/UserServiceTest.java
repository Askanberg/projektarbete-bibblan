package org.bibblan.usermanagement;

import org.bibblan.usermanagement.exception.UserAlreadyExistsException;
import org.bibblan.usermanagement.service.UserService;
import org.bibblan.usermanagement.user.User;
import org.bibblan.usermanagement.userrepository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayName("User Service Test")
public class UserServiceTest {

//    @Autowired
//    MockMvc mockMvc;

    @InjectMocks
    private UserService userService;

    @MockBean
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Registrerar användare lokalt med valid data.")
    public void registerNewLocalUserWithValidData() {
        ResponseEntity<User> u = userService.registerNewUser("Beorn", "Bunke_1337", "myRawPassword", "beorn@somedomain.com");

        assertNotNull(u.getBody());

        assertTrue(() -> Stream.of("Beorn", "Bunke_1337", "myRawPassword", "beorn@somedomain.com").anyMatch(s -> s.equals(u.getBody().getName()) || s.equals(u.getBody().getUsername()) || s.equals(u.getBody().getPassword()) || s.equals(u.getBody().getEmail())),
                "Fel: Testet förväntade sig att alla privata fält skulle stämma.");
    }

    @Test
    @DisplayName("Registrerar användare i databasen med valid data.")
    public void registerNewUserInDatabase() {
        when(userRepository.save(any(User.class))).thenReturn(new User());
        ResponseEntity<User> u = userService.registerNewUser("Bunke", "BunkeLunke", "someRawPassword321", "inte@su.se");
        if(u.getBody() != null){
            assertNotNull(userService.getUserByUsername(u.getBody().getUsername()), "Fel: Testet förväntade sig att användaren fanns i databasen.");
        }
        else throw new NullPointerException("Användaren var null i databasen.");
    }

    @Test
    @DisplayName("Lösenord i databasen är krypterade.")
    public void passwordInDatabaseIsEncrypted(){
        when(userRepository.save(any(User.class))).thenReturn(new User());
        ResponseEntity<User> u = userService.registerNewUser("Johan", "Rohan", "someRawPassword123", "rohan@gmail.com");

        assertNotNull(u.getBody(), "Fel: Testet förväntade sig att användaren fanns i databasen.");
        assertNotEquals("someRawPassword123", u.getBody().getPassword(),
                "Fel: Testet förväntade sig att användarens lösenord var krypterat.");
    }
    @Test
    @DisplayName("Registrerar användare med tomt email-fält")
    public void registerNewUserWithBlankEmailThrowsException() {
        User u = User.builder()
                .name("Beorn")
                .username("Bunke_1337")
                .password("myRawPassword")
                .email("")
                .build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerNewUser("Beorn", "Bunke_1337", "myRawPassword", "");
        });

        assertEquals("Email field is empty.", exception.getMessage(),
                "Fel: Testet förväntade sig att ett IllegalArgumentException kastades med meddelandet \"Email field is empty.\"");
    }

    @Test
    @DisplayName("Registerar användare med invalid email.")
    public void registerNewUserWithInvalidEmailThrowsException() {
        when(userRepository.save(any(User.class))).thenReturn(new User());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerNewUser("Beorn", "Bunke_1337", "myRawPassword", "email");
        });

        assertEquals("Invalid email address", exception.getMessage(),
                "Fel: Testet förväntade sig att ett MethodArgumentNotValidException kastades med meddelandet \"Invalid email address.\"");

    }

    @Test
    public void registerNewUserWithInvalidUserNameThrowsException() {
        when(userService.registerNewUser("Beorn", "", "myRawPassword", "some_email.com"));

        Exception exception = assertThrows(MethodArgumentNotValidException.class, () -> {
        });

        assertEquals("Invalid username.", exception.getMessage(),
                "Fel: Förväntade sig att ett MethodArgumentNotValidException kastades med meddelandet \"Invalid username.\"");

    }

    @Test
    public void getUserWithUsernameFromDatabase() {

        userService.registerNewUser("Dachshund", "Tax", "stalaktitEremit", "tax@hundregister.se");
        Optional<User> u = userService.getUserByUsername("Tax");

        if(u.isPresent()){
            assertEquals(User.builder().name("Dachshund").username("Tax").password("stalaktitEremit").email("tax@hundregister.se").ID(u.get().getID()), u,
                    "Fel: UserService returnerade inte rätt användare.");
        }
        else {
            throw new NullPointerException("Ingen användare hittades med det användarnamnet.");
        }
    }

    @Test
    public void registerAlreadyExistingUser(){
        when(userRepository.save(any(User.class))).thenReturn(new User());
        User u = userService.registerNewUser("Arpe", "Hund_97", "somePassword_123", "hund_97@hotmail.com").getBody();

        assertThrows(UserAlreadyExistsException.class, ()-> userService.registerNewUser("Arpe", "Hund_97", "somePassword_123", "hund_97@hotmail.com"),
                "Fel: Testet förväntade sig att ett exception kastades.");
    }

}

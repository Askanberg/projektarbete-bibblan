package org.bibblan.usermanagement.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bibblan.Main;
import org.bibblan.usermanagement.config.DisableSecurityConfig;
import org.bibblan.usermanagement.dto.UserDto;
import org.bibblan.usermanagement.exception.InvalidUserInputException;
import org.bibblan.usermanagement.exception.UserAlreadyExistsException;
import org.bibblan.usermanagement.exception.UserNotFoundException;
import org.bibblan.usermanagement.mapper.UserMapper;
import org.bibblan.usermanagement.role.Role;
import org.bibblan.usermanagement.repository.RoleRepository;
import org.bibblan.usermanagement.testinitializer.TestContextInitializer;
import org.bibblan.usermanagement.user.User;
import org.bibblan.usermanagement.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {Main.class, DisableSecurityConfig.class, TestContextInitializer.TestConfig.class})
@DisplayName("User Service Test")
@TestPropertySource(locations = "classpath:usermanagement/application-test.properties")
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    private final UserDto FIRST_DTO = UserDto.builder()
            .name("Name")
                .username("Username")
                .email("test@email.com")
                .password("someRawPassword123123")
                .build();
    @Autowired
    private ObjectMapper jacksonObjectMapper;


    @Test
    @DisplayName("getUsers() kastar ett UserNotFoundException när inga användare är registrerade.")
    public void getUsersWithoutUsersThrowsException(){
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        Exception e = assertThrows(UserNotFoundException.class, userService::getUsers
        , "Förväntade att getUsers() skulle kasta ett undantag.");

        assertTrue(e.getMessage().contains("No registered users yet."));

    }

    @Test
    @DisplayName("getUserById() returnerar den korrekta registrerade användaren.")
    public void getUserByIdReturnsCorrectUser(){
        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User mockUser = i.getArgument(0);
            mockUser.setID(1);
            mockUser.setName(FIRST_DTO.getName());
            mockUser.setUsername(FIRST_DTO.getUsername());
            mockUser.setPassword(FIRST_DTO.getPassword());
            mockUser.setEmail(FIRST_DTO.getEmail());
            return mockUser;
        });

        User u = userService.registerNewUser(FIRST_DTO);

        verify(userRepository, times(1)).save(any(User.class));

        assertEquals(1, u.getID(),
                "Förväntade att den enda registerarde användaren hade rätt id.");

        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(u));

        UserDto dto = userService.getUserDTOById(1);

        System.out.println("UserServiceTest DTO = "+ dto);

        assertEquals(u, UserMapper.toEntity(dto),
                "Förväntade att rätt användare skulle returneras.");

    }


    @Test
    @DisplayName("getUserById() kastar ett UserNotFoundException när användaren inte finns.")
    public void getUserByIdThrowsException() {
        UserDto firstDTO = UserDto.builder()
                .name("Name")
                .username("Username")
                .email("test@email.com")
                .password("someRawPassword123123")
                .build();

        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User u = i.getArgument(0);
            u.setName("Name");
            u.setEmail("test@email.com");
            u.setUsername("Username");
            u.setPassword("someRawPassword123123");
            u.setID(1);
            return u;
        });


        User u = userService.registerNewUser(firstDTO);

        verify(userRepository, times(1)).save(any(User.class));

        System.out.println("UserServiceTest = " + u);

        assertNotNull(u,
                "Förväntade att användaren skulle finnas i databasen.");


        assertEquals(1, u.getID()
        ,"Förväntade att användaren ID skulle vara 1.");


        Exception e = assertThrows(UserNotFoundException.class, () -> userService.getUserDTOById( 100),
                "Förväntade att ett UserNotFoundException skulle kastas.");

        assertEquals("No registered user with that ID.", e.getMessage());

    }

    @Test
    @DisplayName("getUsers() returnerar en lista med alla registrerade användare.")
    public void getUsersReturnsRegisteredUsers(){
        when(userRepository.save(any(User.class))).thenReturn(new User());

        UserDto firstDTO = UserDto.builder()
                .name("First Name")
                .username("Second Username")
                .email("first@email.com")
                .password("someRawPassword123")
                .build();

        UserDto secondDTO = UserDto.builder()
                .name("Second Name")
                .username("Second Username")
                .email("second@email.com")
                .password("secondRawPassword123").build();

        UserDto thirdDTO = UserDto.builder()
                        .name("Third Name")
                        .username("Third Username")
                        .email("third@email.com")
                        .password("thirdRawPassword123")
                        .build();

        userService.registerNewUser(firstDTO);
        userService.registerNewUser(secondDTO);
        userService.registerNewUser(thirdDTO);

        verify(userRepository, times(3)).save(any(User.class));

        List<User> users = List.of(UserMapper.toEntity(firstDTO), UserMapper.toEntity(secondDTO), UserMapper.toEntity(thirdDTO));

        when(userRepository.findAll()).thenReturn(users);

        users = userService.getUsers();

        verify(userRepository, times(1)).findAll();

        assertNotNull(users,
                "Listan med användare förväntades inte vara null.");

        assertEquals(3, users.size(),
                "Förväntade att listan skulle innehålla tre användare.");

        assertTrue(users.containsAll(Arrays.asList(UserMapper.toEntity(firstDTO), UserMapper.toEntity(secondDTO), UserMapper.toEntity(thirdDTO))));
    }

    @Test
    @DisplayName("Registerar användare med valid data.")
    public void registerNewSavesUser() {


        // Definierar mock-objektets beteende när UserService::registerNewUser anropas som i sin tur anropar UserRepository::save
        when(userRepository.save(any(User.class))).thenReturn(new User()).then(a -> {
            User u = a.getArgument(0);
            roleRepository.save(new Role("ROLE_MEMBER"));
            u.getRoles().add(new Role("ROLE_MEMBER"));
            return u;
        });

        // Registrerar användaren genom Service-lagret som förväntas sparas sedan i databasen.
        UserDto userDto = new UserDto("inte@su.se", "Bunke", "BunkeLunke", "someRawPassword");
        userDto.getRoles().add(new Role("ROLE_MEMBER"));
        User u = userService.registerNewUser(userDto);

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

        User u = userService.registerNewUser(UserDto.builder().name("Johan").username("Rohan").email("rohan@gmail.com").password("someRawPassword123").build());

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


        assertThrows(NullPointerException.class, () -> userService.registerNewUser(UserDto.builder().name("Name").password("someRawPassword123").email("some@email.com").build()));

        verifyNoInteractions(userRepository);

    }

    @Test
    public void registerNewUserWithoutPasswordThrowsException() {
        when(userRepository.save(any(User.class))).thenReturn(new User());

        Exception e = assertThrows(InvalidUserInputException.class, () -> userService.registerNewUser(UserDto.builder().name("Name").username("Lolipop").email("some@email.com").build()));
        System.out.println(e.getMessage());

        assertEquals("Password field is required.", e.getMessage(),
                "Felmeddelandet var inte som förväntat.");
        verifyNoInteractions(userRepository);

    }




    @Test
    public void getUserWithUsernameReturnsUser() {
        User u = User.builder().name("Some Name").username("Sven").email("someValid@email.com").password("someRawPassword").build();

        when(userRepository.save(any(User.class))).thenReturn(new User());

        UserDto temp = UserMapper.toDTO(u);
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

        Exception e = assertThrows(UserAlreadyExistsException.class, () -> userService.registerNewUser(UserDto.builder()
                .name("Arpe")
                .username("Hund_97")
                .email("hund_97@hotmail.com")
                .password("someRawPassword_1337")
                .build()));

        assertEquals("A user is already registered with that username.", e.getMessage() ,
                "Fel: Förväntade ett UserAlreadyExistsException.");

        verify(userRepository, times(1)).findByUsername(any(String.class));

    }

}

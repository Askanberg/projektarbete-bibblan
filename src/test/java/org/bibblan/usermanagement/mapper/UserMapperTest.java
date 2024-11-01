package org.bibblan.usermanagement.mapper;

import org.bibblan.usermanagement.dto.UserDTO;
import org.bibblan.usermanagement.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    @DisplayName("Omvandlar ett DTO-objekt till Entitet.")
    void toDTOConvertsFromEntityToDTO() {
        User user = User.builder()
                .name("User")
                .username("someUsername")
                .email("some@gmail.com")
                .password(null)
                .build();

        UserDTO userDTO = userMapper.toDTO(user);

        // Kontrollerar att UserDTO har initialiserats.
        assertNotNull(userDTO);

        assertEquals(userDTO.getName(), user.getName());
        assertEquals(userDTO.getUsername(), user.getUsername());
        assertEquals(userDTO.getEmail(), user.getEmail());
    }

    @Test
    void toEntityConvertsFromEntityToDTO() {
        UserDTO userDTO = UserDTO.builder()
                .name("DTO")
                .username("UserDTOname")
                .email("UserDTO@gmail.com")
                .password(null)
                .build();
        User user = userMapper.toEntity(userDTO);

        assertEquals(userDTO.getName(), user.getName());
        assertEquals(userDTO.getUsername(), user.getUsername());
        assertEquals(userDTO.getEmail(), user.getEmail());
    }

    @Test
    void toEntityLeavesOutPassword(){
        UserDTO userDTO = UserDTO.builder()
                .username("Username")
                .name("Name")
                .email("Email@gmail.com")
                .password("leaveOutPassword")
                .build();

        UserMapper userMapper = new UserMapper();

        User user = userMapper.toEntity(userDTO);

        assertNotNull(user
                , "Fel: Användaren har inte initialiserats.");

        assertNotEquals(user.getPassword(), userDTO.getPassword(),
                "Fel: Lösenordet förväntades vara null efter mappningen.");
    }

    @Test
    @DisplayName("Lösenordet sparas inte när User mappas till DTO")
    void toDTOLeavesOutPassword(){
        User u = User.builder()
                .username("Username")
                .name("Name")
                .email("some@gmail.com")
                .password("leaveOutPassword")
                .build();


        UserDTO userDTO = userMapper.toDTO(u);

        assertNotNull(userDTO
        , "Fel: Förväntade att DTO-objektet fanns.");

        assertNotEquals(u.getPassword(), userDTO.getPassword(),
                "Fel: Lösenordet förväntades vara null efter mappningen.");
    }
    @Test
    @DisplayName("Konvertering till Entity som är null ger en tom DTO.")
    public void nullToEntityReturnsEmptyDTO(){
        User u = userMapper.toEntity(null);

        assertNotNull(u);

        assertEquals(u, new User());

    }

    @Test
    @DisplayName("Konvertering till DTO som är null ger en tom Entity.")
    public void nullToDTOReturnsEmptyEntity(){
        UserDTO u = userMapper.toDTO(null);

        assertNotNull(u);

        assertEquals(u, new UserDTO());

    }
}
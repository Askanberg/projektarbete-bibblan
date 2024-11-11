package org.bibblan.usermanagement.mapper;

import org.bibblan.usermanagement.dto.UserDto;
import org.bibblan.usermanagement.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    @DisplayName("Omvandlar ett DTO-objekt till Entitet.")
    void toDTOConvertsFromEntityToDTO() {
        User user = User.builder()
                .name("User")
                .username("someUsername")
                .email("some@gmail.com")
                .password(null)
                .build();

        UserDto userDTO = UserMapper.toDTO(user);

        // Kontrollerar att UserDTO har initialiserats.
        assertNotNull(userDTO);

        assertEquals(userDTO.getName(), user.getName());
        assertEquals(userDTO.getUsername(), user.getUsername());
        assertEquals(userDTO.getEmail(), user.getEmail());
    }

    @Test
    void toEntityConvertsFromEntityToDTO() {
        UserDto userDTO = UserDto.builder()
                .name("DTO")
                .username("UserDTOname")
                .email("UserDTO@gmail.com")
                .password(null)
                .build();
        User user = UserMapper.toEntity(userDTO);

        assertEquals(userDTO.getName(), user.getName());
        assertEquals(userDTO.getUsername(), user.getUsername());
        assertEquals(userDTO.getEmail(), user.getEmail());
    }

    @Test
    void toEntityLeavesOutPassword(){
        UserDto userDTO = UserDto.builder()
                .username("Username")
                .name("Name")
                .email("Email@gmail.com")
                .password("leaveOutPassword")
                .build();

        User user = UserMapper.toEntity(userDTO);

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


        UserDto userDTO = UserMapper.toDTO(u);

        assertNotNull(userDTO
        , "Fel: Förväntade att DTO-objektet fanns.");

        assertNotEquals(u.getPassword(), userDTO.getPassword(),
                "Fel: Lösenordet förväntades vara null efter mappningen.");
    }
    @Test
    @DisplayName("Konvertering till Entity som är null ger en tom DTO.")
    public void nullToEntityReturnsEmptyDTO(){
        User u = UserMapper.toEntity(null);

        assertNotNull(u);

        assertEquals(u, new User());

    }

    @Test
    @DisplayName("Konvertering till DTO som är null ger en tom Entity.")
    public void nullToDTOReturnsEmptyEntity(){
        UserDto u = UserMapper.toDTO(null);

        assertNotNull(u);

        assertEquals(u, new UserDto());

    }
}
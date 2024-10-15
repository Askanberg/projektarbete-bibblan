package org.bibblan.usermanagement;

import org.bibblan.usermanagement.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Password Encoding Test")
public class BCryptPasswordEncodingTest {

    @Test
    public void testPasswordEncoder(){
        final User user = User.builder()
                .userName("Test")
                .name("Test")
                .ID(101)
                .email("something@domain.com")
                .password("myRawPassword")
                .build();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        assertTrue(passwordEncoder.matches(user.getPassword(), encodedPassword),
                "Fel: Testet förväntade sig att det avkrypterade lösenordet skulle stämma överens.");

    }
}

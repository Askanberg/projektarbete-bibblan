package org.bibblan.usermanagement.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("User Management Test")
public class UserTest {
    private final User user = User.builder() // Dummy-objekt utan ID eftersom det inte initialiseras utanför databas-integrationen.
            .email("anna@gmail.com")
            .username("Anna_Book")
            .name("Anna Book")
            .password("SzrqTlst_#")
            .build();

    @Test
    public void emailInitializedCorrectly() {

        Assertions.assertEquals(user.getEmail(), "anna@gmail.com",
                "Fel: Testet förväntade sig att email skulle vara \"anna@gmail.com\"");

    }

    @Test
    public void userNameInitializedCorrectly() {

        Assertions.assertEquals(user.getUsername(), "Anna_Book",
                "Fel: Testet förväntade sig att användarnamnet skulle vara \"Anna_Book\"");
    }

    @Test
    public void nameInitializedCorrectly() {

        Assertions.assertEquals(user.getName(), "Anna Book",
                "Fel: Testet förväntade sig att namnet skulle vara \"Anna book\"");
    }

    @Test
    public void passwordInitializedCorrectly() {

        Assertions.assertEquals(user.getPassword(), "SzrqTlst_#",
                "Fel: Testet förväntade sig att lösenordet skulle vara \"SzrqTlst_#\"");
    }


}

package org.bibblan;

import org.bibblan.bookcatalog.ItemCollection;
import org.bibblan.bookcatalog.ItemFactory;
import org.bibblan.bookcatalog.ItemFileReader;
import org.bibblan.usermanagement.user.User;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

public class IntegrationTests {
    private ItemCollection itemCollection;
    private ItemFileReader itemFileReader;


    @BeforeEach
    void setup() throws IOException {
        itemCollection = new ItemCollection();
        itemFileReader = new ItemFileReader(new ItemFactory());
        itemFileReader.readItemsFromCsv("src/test/resources/testBooks.csv");

        User userOne = User.builder().name("Henke").username("Benke").email("some@email.com").password("someRawPassword").build();
        User userTwo = User.builder().name("Andreas").username("Senapsberg").email("skanberg@su.se").password("someRasPassword123").build();
        User userThree = User.builder().name("Sick Sten").username("Six_Ten^").email("six_ten@gmail.com").password("someRawPassword1337").build();
        User userFour = User.builder().name("Fina Jose").username("Ferber").email("Ferber@domain.se").password("someRawPassword_1337").build();
        User userFive = User.builder().name("Bunke").username("Fil_Bunke").email("testUser@hotmail.com").password("aRawPassword1337").build();
        User userSix = User.builder().name("Bibblan").username("Projektarbete").email("projektarbete@bibblan.se").password("someRawPassword1337").build();
    }


}

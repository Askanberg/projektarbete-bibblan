package org.bibblan;

import org.bibblan.bookcatalog.ItemCollection;
import org.bibblan.bookcatalog.ItemFactory;
import org.bibblan.bookcatalog.ItemFileReader;
import org.bibblan.usermanagement.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class IntegrationTests {
    private ItemCollection itemCollection;
    private ItemFileReader itemFileReader;
    private User userOne;
    private User userTwo;
    private User userThree;
    private User userFour;
    private User userFive;
    private User userSix;

    @BeforeEach
    void setup() throws IOException {
        itemCollection = new ItemCollection();
        itemFileReader = new ItemFileReader(new ItemFactory());
        itemCollection.addItems(itemFileReader.readItemsFromCsv("src/test/resources/testBooks.csv"));
        itemCollection.addItems(itemFileReader.readItemsFromCsv("src/test/resources/testEBooks.csv"));

        userOne = User.builder().name("Henke").username("Benke").email("some@email.com").password("someRawPassword").build();
        userTwo = User.builder().name("Andreas").username("Senapsberg").email("skanberg@su.se").password("someRawPassword123").build();
        userThree = User.builder().name("Sick Sten").username("Six_Ten^").email("six_ten@gmail.com").password("someRawPassword1337").build();
        userFour = User.builder().name("Fina Jose").username("Ferber").email("Ferber@domain.se").password("someRawPassword_1337").build();
        userFive = User.builder().name("Bunke").username("Fil_Bunke").email("testUser@hotmail.com").password("aRawPassword1337").build();
        userSix = User.builder().name("Bibblan").username("Projektarbete").email("projektarbete@bibblan.se").password("someRawPassword1337").build();
    }

    @Test
    void test() {
    }


}

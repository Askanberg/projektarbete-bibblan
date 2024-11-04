package org.bibblan;

import org.bibblan.bookcatalog.ItemCollection;
import org.bibblan.bookcatalog.ItemFactory;
import org.bibblan.bookcatalog.ItemFileReader;
import org.bibblan.bookcatalog.domain.Book;
import org.bibblan.bookcatalog.domain.Item;
import org.bibblan.loanmanagement.Loan;
import org.bibblan.loanmanagement.LoanCollections;
import org.bibblan.reviews.RecommendationSystem;
import org.bibblan.reviews.Review;
import org.bibblan.reviews.ReviewCollection;
import org.bibblan.usermanagement.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class IntegrationTests {
    private ItemCollection itemCollection;
    private ItemFileReader itemFileReader;
    private User userOne;
    private User userTwo;
    private User userThree;
    private User userFour;
    private User userFive;
    private User userSix;
    private RecommendationSystem recommendationSystem;
    private ReviewCollection reviewCollection;

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

        reviewCollection = new ReviewCollection();
        recommendationSystem = new RecommendationSystem(reviewCollection);

        List<Item> itemsByTitle = itemCollection.sortItems();
        // User One
        addReview(itemsByTitle.get(0), 3, userOne); // Item 1
        addReview(itemsByTitle.get(1), 1, userOne); // Item 2
        addReview(itemsByTitle.get(2), 4, userOne); // Item 3
        addReview(itemsByTitle.get(3), 2, userOne); // Item 4
        addReview(itemsByTitle.get(4), 5, userOne); // Item 5
        // User Two
        addReview(itemsByTitle.get(0), 4, userTwo); // Item 1
        addReview(itemsByTitle.get(1), 3, userTwo); // Item 2
        addReview(itemsByTitle.get(2), 5, userTwo); // Item 3
        addReview(itemsByTitle.get(3), 3, userTwo); // Item 4
        addReview(itemsByTitle.get(4), 4, userTwo); // Item 5
        // User Three
        addReview(itemsByTitle.get(0), 1, userThree); // Item 1
        addReview(itemsByTitle.get(1), 1, userThree); // Item 2
        addReview(itemsByTitle.get(2), 5, userThree); // Item 3
        addReview(itemsByTitle.get(4), 3, userThree); // Item 5
        // User Four
        addReview(itemsByTitle.get(1), 4, userFour); // Item 2
        addReview(itemsByTitle.get(2), 1, userFour); // Item 3
        addReview(itemsByTitle.get(3), 5, userFour); // Item 4
        // User Five
        addReview(itemsByTitle.get(0), 3, userFive); // Item 1
        addReview(itemsByTitle.get(4), 5, userFive); // Item 5
        // User Six
        addReview(itemsByTitle.get(4), 1, userSix); // Item 5
        addReview(itemsByTitle.get(5), 5, userSix); // Item 6

    }

    private void addReview(Item item, int rating, User user) {
        Review review = new Review(item, rating, user);
        reviewCollection.addReview(review);
    }

    @Test
    void test() {
        LoanCollections loanCollections = new LoanCollections();
        loanCollections.addLoan((Book) itemCollection.getItemMap().get("Becoming").get(0));
        System.out.println(loanCollections.getAllActiveLoans());

        loanCollections.returnLoan("Becoming");
        System.out.println(loanCollections.getAllActiveLoans());

    }

}

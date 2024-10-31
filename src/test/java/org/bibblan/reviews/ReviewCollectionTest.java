package org.bibblan.reviews;

import org.bibblan.bookcatalog.domain.Item;
import org.bibblan.usermanagement.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ReviewCollectionTest {

    @Mock
    private User user1;
    @Mock
    private User user2;
    @Mock
    private User user3;
    @Mock
    private Item item1;
    @Mock
    private Item item2;
    @Mock
    private Item item3;
    private Review review1;
    private Review review2;
    private Review review3;


    private ReviewCollection reviewCollection;

    @BeforeEach
    void setUp() {
        reviewCollection = new ReviewCollection();
        review1 = new Review(item1, 5, user1);
        review2 = new Review(item2, 4, user1);
        review3 = new Review(item2, 1, user2);
        reviewCollection.addReview(review1);
        reviewCollection.addReview(review2);
        reviewCollection.addReview(review3);
    }

    @Test
    void testAddReviewToCollection() { //TF1
        assertTrue(reviewCollection.getReviewsByItem(item1).contains(review1), "Review was not added to the Item collection");
        assertTrue(reviewCollection.getReviewsByUser(user1).contains(review1), "Review was not added to the User Collection");
    }

    @Test
    void testAddDuplicateReviewToCollection() { //TF2
        reviewCollection.addReview(review1);

        assertEquals(1, reviewCollection.getReviewsByItem(item1).size(), "Duplicate review should not be added");
    }

    @Test
    void testAddNullReviewToCollection() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> reviewCollection.addReview(null),
                "Expected addReview to throw an exception when given a null review");

        assertEquals("Review cannot be null!", thrown.getMessage());
    }

    @Test
    void testGetReviewsByUser() { //TF3

        HashSet<Review> userReviews = new HashSet<>(reviewCollection.getReviewsByUser(user1));

        assertTrue(userReviews.contains(review1), "User1's reviews should include review1");
        assertTrue(userReviews.contains(review2), "User1's reviews should include review2");
        assertFalse(userReviews.contains(review3), "User1's reviews should not include review3");

    }

    @Test
    void testGetReviewsByUserWithNoReviews() { //TF4

        HashSet<Review> userReviews = new HashSet<>(reviewCollection.getReviewsByUser(user3));

        assertTrue(userReviews.isEmpty(), "User3's reviews should be empty");
    }

    @Test
    void testGetReviewsByItem() { //TF5

        HashSet<Review> itemReviews = new HashSet<>(reviewCollection.getReviewsByItem(item1));

        assertTrue(itemReviews.contains(review1), "item1's reviews should include review1");
        assertFalse(itemReviews.contains(review2), "item1's reviews should not include review2");
        assertFalse(itemReviews.contains(review3), "item1's reviews should not include review3");

    }

    @Test
    void testGetReviewsByItemWithNoReviews() { //TF6

        HashSet<Review> itemReviews = new HashSet<>(reviewCollection.getReviewsByItem(item3));

        assertTrue(itemReviews.isEmpty(), "User3's reviews should be empty");
    }

    @Test
    void testGetAverageRatingOfItem() {
        Review review4 = new Review(item1, 2, user1);
        reviewCollection.addReview(review4);

        assertEquals(2.5, reviewCollection.getAverageRating(item2), "Item2's average rating should be 2.5");
        assertEquals(3.5, reviewCollection.getAverageRating(item1), "Item1's average rating should be 3.5");
    }

    @Test
    void testGetAverageRatingWithNoReviews() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> reviewCollection.getAverageRating(item3),
                "Expected getAverageRating to throw an exception if no reviews are available");

        assertEquals("No reviews available for this item.", thrown.getMessage());
    }

    @Test
    void testGetAllUsers() {
        HashSet<User> users = new HashSet<>(reviewCollection.getAllUsers());
        assertTrue(users.contains(user1), "Users should include user1");
        assertTrue(users.contains(user2), "Users should include user2");
    }

    @Test
    void testGetItemsByUser() {
        HashSet<Item> items = new HashSet<>(reviewCollection.getItemsByUser(user1));
        assertTrue(items.contains(item1), "User1's items should include item1");
        assertTrue(items.contains(item2), "User1's items should include item2");
        assertEquals(2, items.size(), "There should only be 2 items inside User1's items");
    }

}
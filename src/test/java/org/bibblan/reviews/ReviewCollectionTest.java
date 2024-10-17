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
    private String comment;
    private int rating;
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
        assertTrue(reviewCollection.containsReview(review1), "Review was not added to collection");
    }

    @Test
    void testAddDuplicateReviewToCollection() { //TF2
        reviewCollection.addReview(review1);

        assertEquals(1, reviewCollection.getReviewsByItem(item1).size(), "Duplicate review should not be added");
    }

    @Test
    void testGetReviewsByUser() { //TF3

        HashSet<Review> userReviews = new HashSet<>(reviewCollection.getReviewsByUser(user1));

        assertTrue(userReviews.contains(review1), "User1's reviews should include review1");
        assertTrue(userReviews.contains(review2), "User1's reviews should include review2");
        assertFalse(userReviews.contains(review3), "User1's reviews should not include review3");

    }

    @Test
    void testGetReviewsByUserWithNoReviews() {

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
    void testGetReviewsByItemWithNoReviews() {

        HashSet<Review> itemReviews = new HashSet<>(reviewCollection.getReviewsByItem(item3));

        assertTrue(itemReviews.isEmpty(), "User3's reviews should be empty");
    }

}
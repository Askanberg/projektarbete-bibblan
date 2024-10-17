package org.bibblan.reviews;

import org.bibblan.bookcatalog.domain.Book;
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
    private Book book1;
    @Mock
    private Book book2;
    private String comment;
    private int rating;

    private ReviewCollection reviewCollection;

    @BeforeEach
    void setUp() {
        reviewCollection = new ReviewCollection();
    }

    @Test
    void testAddReviewToCollection() {
        Review review = new Review(book1, 5, user1);

        reviewCollection.addReview(review);

        assertTrue(reviewCollection.containsReview(review), "Review was not added to collection");
    }

    @Test
    void testGetReviewsByUser() {
        Review review1 = new Review(book1, 5, user1);
        Review review2 = new Review(book2, 4, user1);
        Review review3 = new Review(book2, 1, user2);
        reviewCollection.addReview(review1);
        reviewCollection.addReview(review2);
        reviewCollection.addReview(review3);

        HashSet<Review> userReviews = new HashSet<>(getReviewsByUser(user1));

        assertTrue(userReviews.contains(review1), "User1's reviews should include review1");
        assertTrue(userReviews.contains(review2), "User1's reviews should include review2");
        assertFalse(userReviews.contains(review3), "User1's reviews should not include review3");

    }
}

package org.bibblan.reviews;

import org.bibblan.bookcatalog.domain.Item;
import org.bibblan.usermanagement.user.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ReviewTest {

    @Mock
    private User user;
    @Mock
    private Item item, item2;
    private String comment;
    private int rating;


    @Test
    void testReviewCreationWithValidData() { //TF1
        comment = "Good item!";
        rating = 5;
        Review review = new Review(item, rating, user, comment);

        assertNotNull(review, "Review is null");
        assertEquals(5, review.getRating(), "Rating does not match input");
        assertEquals(item, review.getItem(), "item does not match input");
        assertEquals(user, review.getUser(), "User does not match input");
        assertEquals("Good item!", review.getComment(), "Comment does not match input");
    }

    @Test
    void testReviewCreationWithoutComment() { //TF2

        Review review = new Review(item, 1, user);

        assertNotNull(review, "Review is null");
        assertEquals("", review.getComment(), "Comment is not empty");
    }

    @Test
    void testReviewCreationWithTooHighRating() { //TF3
        rating = 6;
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Review(item, rating, user, comment));

        assertEquals("Rating must be between 1 and 5!", thrown.getMessage());
    }

    @Test
    void testReviewCreationWithTooLowRating() { //TF4
        rating = 0;
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Review(item, rating, user, comment));

        assertEquals("Rating must be between 1" +
                " and 5!", thrown.getMessage());
    }

    @Test
    void testReviewCreationWithTooLongComment() { //TF5
        comment = "x".repeat(501);
        rating = 1;
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Review(item, rating, user, comment));

        assertEquals("Comment must be between 1 and 500 characters!", thrown.getMessage());
    }

    @Test
    void testReviewCreationWithMaximumCommentLength() {
        comment = "x".repeat(500);
        rating = 3;
        Review review = new Review(item, rating, user, comment);

        assertNotNull(review, "Review is null");
        assertEquals(500, review.getComment().length(), "Comment length does not match boundary");
        assertEquals(comment, review.getComment(), "Comment content does not match input");
    }

    @Test
    void testReviewCreationWithNullitem() { //TF6
        item = null;
        rating = 3;

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Review(item, rating, user, comment));
        assertEquals("item cannot be null!", thrown.getMessage());
    }

    @Test
    void testReviewCreationWithNullUser() { //TF7
        user = null;
        rating = 3;

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Review(item, rating, user, comment));
        assertEquals("User cannot be null!", thrown.getMessage());
    }

    @Test
    void testReviewIdCreation() {
        Review.resetIdCounter();
        Review review = new Review(item, 5, user);
        Review review2 = new Review(item2, 2, user);
        assertEquals(1, review.getId());
        assertEquals(2, review2.getId());
    }
}
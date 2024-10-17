package org.bibblan.reviews;

import org.bibblan.bookcatalog.domain.Item;
import org.bibblan.usermanagement.User;
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
    private Item item;
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
}
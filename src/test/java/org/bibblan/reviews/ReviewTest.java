package org.bibblan.reviews;

import org.bibblan.bookcatalog.domain.Book;
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
    private Book book;
    private String comment;
    private int rating;


    @Test
    void testReviewCreationWithValidData() { //TF1
        comment = "Good book!";
        rating = 5;
        Review review = new Review(book, rating, user, comment);

        assertNotNull(review, "Review is null");
        assertEquals(5, review.getRating(), "Rating does not match input");
        assertEquals(book, review.getBook(), "Book does not match input");
        assertEquals(user, review.getUser(), "User does not match input");
        assertEquals("Good book!", review.getComment(), "Comment does not match input");
    }

    @Test
    void testReviewCreationWithoutComment() { //TF2

        Review review = new Review(book, 1, user);

        assertNotNull(review, "Review is null");
        assertEquals("", review.getComment(), "Comment is not empty");
    }

    @Test
    void testReviewCreationWithTooHighRating() {
        rating = 6;
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Review(book, rating, user, comment));

        assertEquals("Rating must be between 0 and 5!", thrown.getMessage());
    }
}

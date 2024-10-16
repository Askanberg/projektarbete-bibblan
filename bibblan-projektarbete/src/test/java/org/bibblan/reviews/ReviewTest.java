package org.bibblan.reviews;

import org.bibblan.bookcatalog.Book;
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


    @Test
    void testReviewCreationWithValidData() {

        Review review = new Review(book, 5, "Good book!", user);

        assertNotNull(review, "Review is null");
        assertEquals(book, review.getBook(), "Book does not match input");
        assertEquals(user, review.getUser(), "User does not match input");
        assertEquals("Good book!", review.getComment(), "Comment does not match input");
    }

}
package org.bibblan.reviews;

import org.bibblan.bookcatalog.domain.Book;
import org.bibblan.usermanagement.User;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ReviewCollectionTest {

    @Mock
    private User user;
    @Mock
    private Book book;
    private String comment;
    private int rating;


    @Test
    void testAddReviewToCollection() {
        ReviewCollection reviewCollection = new ReviewCollection();
        Review review = new Review(book, 5, user);

        reviewCollection.addReview(review);

        assertTrue(reviewCollection.containsReview(review), "Review was not added to collection");
    }
}

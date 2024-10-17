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
    private Review review1;
    private Review review2;
    private Review review3;


    private ReviewCollection reviewCollection;

    @BeforeEach
    void setUp() {
        reviewCollection = new ReviewCollection();
        review1 = new Review(book1, 5, user1);
        review2 = new Review(book2, 4, user1);
        review3 = new Review(book2, 1, user2);
        reviewCollection.addReview(review1);
        reviewCollection.addReview(review2);
        reviewCollection.addReview(review3);
    }

    @Test
    void testAddReviewToCollection() {
        assertTrue(reviewCollection.containsReview(review1), "Review was not added to collection");
    }

    @Test
    void testGetReviewsByUser() {

        HashSet<Review> userReviews = new HashSet<>(reviewCollection.getReviewsByUser(user1));

        assertTrue(userReviews.contains(review1), "User1's reviews should include review1");
        assertTrue(userReviews.contains(review2), "User1's reviews should include review2");
        assertFalse(userReviews.contains(review3), "User1's reviews should not include review3");

    }

    @Test
    void testGetReviewsByBook() {

        HashSet<Review> bookReviews = new HashSet<>(reviewCollection.getReviewsByBook(book1));

        assertTrue(bookReviews.contains(review1), "Book1's reviews should include review1");
        assertFalse(bookReviews.contains(review2), "Book1's reviews should not include review2");
        assertFalse(bookReviews.contains(review3), "Book1's reviews should not include review3");

    }

}
package org.bibblan.reviews;

import org.bibblan.bookcatalog.domain.Item;
import org.bibblan.usermanagement.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecommendationSystemTest {

    private RecommendationSystem recommendationSystem;
    private ReviewCollection reviewCollection;
    @Mock
    private User user1, user2, user3, user4, user5;
    @Mock
    private Item item1, item2, item3, item4, item5;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
        reviewCollection = new ReviewCollection();
        recommendationSystem = new RecommendationSystem(reviewCollection);

        addReview(item1, 4, user1);
        addReview(item2, 3, user1);
        addReview(item3, 5, user1);
        addReview(item1, 4, user2);
        addReview(item3, 2, user2);
        addReview(item4, 3, user2);
        addReview(item1, 5, user3);
        addReview(item2, 3, user3);
        addReview(item5, 1, user4);
        addReview(item2, 2, user4);
        addReview(item2, 3, user5);
        addReview(item3, 4, user5);

    }

    private void addReview(Item item, int rating, User user) {
        Review review = new Review(item, rating, user); // No mocking here
        reviewCollection.addReview(review);
    }

    @Test
    void testNoCommonItems() {
        double similarity = recommendationSystem.calculateSimilarity(user2, user4);
        assertEquals(0, similarity, "Similarity should be 0 when there are no common items");
    }

}

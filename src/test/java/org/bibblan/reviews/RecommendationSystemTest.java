package org.bibblan.reviews;

import org.bibblan.bookcatalog.domain.Item;
import org.bibblan.usermanagement.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RecommendationSystemTest {

    private RecommendationSystem recommendationSystem;
    private ReviewCollection reviewCollection;
    @Mock
    private User user1, user2, user3, user4, user5, user6, user7;
    @Mock
    private Item item1, item2, item3, item4, item5;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
        reviewCollection = new ReviewCollection();
        recommendationSystem = new RecommendationSystem(reviewCollection);

        // User 1
        addReview(item1, 3, user1); // Item 1
        addReview(item2, 1, user1); // Item 2
        addReview(item3, 4, user1); // Item 3
        addReview(item4, 2, user1); // Item 4
        addReview(item5, 5, user1); // Item 5

        // User 2
        addReview(item1, 4, user2); // Item 1
        addReview(item2, 3, user2); // Item 2
        addReview(item3, 5, user2); // Item 3
        addReview(item4, 3, user2); // Item 4
        addReview(item5, 4, user2); // Item 5

        // User 3
        addReview(item1, 1, user3); // Item 1
        addReview(item2, 1, user3); // Item 2
        addReview(item3, 5, user3); // Item 3
        addReview(item5, 3, user3); // Item 5

        // User 4
        addReview(item2, 4, user4); // Item 2
        addReview(item3, 1, user4); // Item 3
        addReview(item4, 5, user4); // Item 4

        // User 5
        addReview(item1, 3, user5); // Item 1
        addReview(item5, 5, user5); // Item 5

        // User 6
        addReview(item5, 1, user6); // Item 5

    }

    private void addReview(Item item, int rating, User user) {
        Review review = new Review(item, rating, user); // No mocking here
        reviewCollection.addReview(review);
    }

    @Test
    void testNoCommonItems() {
        double similarity = recommendationSystem.calculateSimilarity(user4, user5);
        assertEquals(0, similarity, "Similarity should be 0 when there are no common items");
    }

    //Testar att koefficienten blir 1 då user1 och user5 har samma ratings på item1 och item5
    @Test
    void testReviewsWithSameRatings() {
        double similarity = recommendationSystem.calculateSimilarity(user1, user5);
        assertEquals(1, similarity, 0.0001, "Similarity should be 1 when the reviews have the same rating");
    }

    @Test
    void testReviewsWithOppositeRatings() {
        double similarity = recommendationSystem.calculateSimilarity(user3, user4);
        assertEquals(-1, similarity, "Similarity should be -1 when the reviews are opposite ratings");
    }

    @Test
    void testSimilarityWithSelf() {
        double similarity = recommendationSystem.calculateSimilarity(user1, user1);
        assertEquals(1, similarity, 0.0001, "Similarity with self should be 1");
    }

    @Test
    void testSimilarRatings() {
        double similarity = recommendationSystem.calculateSimilarity(user1, user2);
        assertEquals(0.7559, similarity, 0.0001, "Similarity should be 0.7559 for user1 and user2");
    }

    @Test
    void testSingleCommonItem() {
        double similarity = recommendationSystem.calculateSimilarity(user2, user6);
        assertEquals(0, similarity, "Similarity should be 0 when there is only 1 common item");

    }

    @Test
    void testUserWthNoReviews() {
        assertThrows(IllegalArgumentException.class, () -> recommendationSystem.calculateSimilarity(user1, user7));
    }

}
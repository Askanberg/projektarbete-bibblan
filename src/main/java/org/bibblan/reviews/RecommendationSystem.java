package org.bibblan.reviews;

import org.bibblan.reviews.*;
import org.bibblan.bookcatalog.domain.Item;
import org.bibblan.usermanagement.User;

import java.util.*;
import java.util.stream.Collectors;

public class RecommendationSystem {

    private final ReviewCollection reviewCollection;

    public RecommendationSystem(ReviewCollection reviewCollection) {
        this.reviewCollection = reviewCollection;
    }

    public double calculateSimilarity(User user1, User user2) {
        Set<Review> user1Reviews = reviewCollection.getReviewsByUser(user1);
        Set<Review> user2Reviews = reviewCollection.getReviewsByUser(user2);

        Set<Item> itemsInCommon = user1Reviews.stream()
                .map(Review::getItem)
                .distinct()
                .filter(item -> user2Reviews.stream().anyMatch(review -> review.getItem().equals(item)))
                .collect(Collectors.toSet());

        if (itemsInCommon.isEmpty()) {
            return 0;
        }

        List<Double> user1Ratings = new ArrayList<>();
        List<Double> user2Ratings = new ArrayList<>();

        for (Item item : itemsInCommon) {
            double rating1 = user1Reviews.stream()
                    .filter(review -> review.getItem().equals(item))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Review not found for user1 and item: " + item))
                    .getRating();
            double rating2 = user2Reviews.stream()
                    .filter(review -> review.getItem().equals(item))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Review not found for user2 and item: " + item))
                    .getRating();
            user1Ratings.add(rating1);
            user2Ratings.add(rating2);
        }

        return 1;

    }

}
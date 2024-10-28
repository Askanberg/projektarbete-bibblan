package org.bibblan.reviews;

import org.bibblan.bookcatalog.domain.Item;
import org.bibblan.usermanagement.User;

import java.util.*;
import java.util.stream.Collectors;

public class RecommendationSystem {

    private final ReviewCollection reviewCollection;

    public RecommendationSystem(ReviewCollection reviewCollection) {
        this.reviewCollection = reviewCollection;
    }

    //Räknar ut similarity mellan två users reviews om de har minst 2 gemensamma items de skapat en review för.
    public double calculateSimilarity(User user1, User user2) {
        Set<Review> user1Reviews = reviewCollection.getReviewsByUser(user1);
        Set<Review> user2Reviews = reviewCollection.getReviewsByUser(user2);
        if (user1Reviews.isEmpty()) {
            throw new IllegalArgumentException("No reviews found by user:" + user1);
        }
        if (user2Reviews.isEmpty()) {
            throw new IllegalArgumentException("No reviews found by user:" + user2);
        }

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

        //GPT-kod för uträkning av Pearsons korrelationskoefficient
        double mean1 = user1Ratings.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double mean2 = user2Ratings.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        double numerator = 0.0;
        double denominator1 = 0.0;
        double denominator2 = 0.0;

        for (int i = 0; i < user1Ratings.size(); i++) {
            double diff1 = user1Ratings.get(i) - mean1;
            double diff2 = user2Ratings.get(i) - mean2;

            numerator += diff1 * diff2;
            denominator1 += diff1 * diff1;
            denominator2 += diff2 * diff2;
        }

        double denominator = Math.sqrt(denominator1) * Math.sqrt(denominator2);
        return (denominator == 0) ? 0 : numerator / denominator;

    }

    public List<Item> getRecommendations(User user) {
        Map<User, Double> userSimilarities = new HashMap<>();
        for(User other : reviewCollection.getAllUsers()) {
            if(!other.equals(user)) {
                double similarity = calculateSimilarity(user, other);
                userSimilarities.put(other, similarity);
            }
        }

        List<User> sortedUsers = userSimilarities.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .toList();

        Set<Item> recommendations = new HashSet<>();
        for(User other : sortedUsers) {
            Set<Review> reviews = reviewCollection.getReviewsByUser(other);
            for(Review review : reviews) {
                if(!reviewCollection.getItemsByUser(user).contains(review.getItem()) && review.getRating() >= 3) {
                    recommendations.add(review.getItem());

                    if(recommendations.size() >= 3) {
                        return new ArrayList<>(recommendations);
                    }
                }
            }
        }
        return new ArrayList<>(recommendations);
    }
}
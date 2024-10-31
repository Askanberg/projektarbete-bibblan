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
        // Plockar fram användarnas reviews, kastar exception om inga finns
        Set<Review> user1Reviews = getReviewsOrThrow(user1);

        Set<Review> user2Reviews = getReviewsOrThrow(user2);

        // Skapar det set som innehåller de items som både user1 och user2 har en review på.
        Set<Item> itemsInCommon = getItemsInCommon(user1Reviews, user2Reviews);

        if (itemsInCommon.isEmpty()) {
            return 0;
        }

        // Hämtar betygen som båda users gett till gemensamma reviews
        List<Double> user1Ratings = getUserRatings(itemsInCommon, user1Reviews);
        List<Double> user2Ratings = getUserRatings(itemsInCommon, user2Reviews);

        // Returnerar Pearsons korrelations-koefficient mellan user 1 och user 2.
        return calculatePearsonCorrelation(user1Ratings, user2Ratings);
    }

    private Set<Review> getReviewsOrThrow(User user) {
        Set<Review> reviews = reviewCollection.getReviewsByUser(user);
        if (reviews.isEmpty()) {
            throw new IllegalArgumentException("No reviews found by user: " + user);
        }
        return reviews;
    }

    private Set<Item> getItemsInCommon(Set<Review> user1Reviews, Set<Review> user2Reviews) {
        return user1Reviews.stream()
                .map(Review::getItem)
                .distinct()
                .filter(item -> user2Reviews.stream().anyMatch(review -> review.getItem().equals(item)))
                .collect(Collectors.toSet());
    }

    private List<Double> getUserRatings(Set<Item> itemsInCommon, Set<Review> userReviews) {
        List<Double> ratings = new ArrayList<>();
        for (Item item : itemsInCommon) {
            double rating = userReviews.stream()
                    .filter(review -> review.getItem().equals(item))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Review not found for user and item: " + item))
                    .getRating();
            ratings.add(rating);
        }
        return ratings;
    }

    // GPT-genererad kod för uträkning av Pearsons korrelations-koefficient
    private double calculatePearsonCorrelation(List<Double> user1Ratings, List<Double> user2Ratings) {
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
        for (User other : reviewCollection.getAllUsers()) {
            if (!other.equals(user)) {
                double similarity = calculateSimilarity(user, other);
                userSimilarities.put(other, similarity);
            }
        }

        List<User> sortedUsers = userSimilarities.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .toList();

        Set<Item> recommendations = new HashSet<>();
        for (User other : sortedUsers) {
            Set<Review> reviews = reviewCollection.getReviewsByUser(other);
            List<Review> reviewList = new ArrayList<>(reviews);
            reviewList.sort(Comparator.comparingInt(Review::getId));
            for (Review review : reviewList) {
                if (!reviewCollection.getItemsByUser(user).contains(review.getItem()) && review.getRating() >= 3) {
                    recommendations.add(review.getItem());

                    if (recommendations.size() >= 3) {
                        return new ArrayList<>(recommendations);
                    }
                }
            }
        }
        return new ArrayList<>(recommendations);
    }
}
package org.bibblan.reviews;

import org.bibblan.bookcatalog.domain.Item;
import org.bibblan.usermanagement.user.*;

import java.util.*;

public class ReviewCollection {

    private final Map<Item, Set<Review>> reviewsByItem = new HashMap<>();
    private final Map<User, Set<Review>> reviewsByUser = new HashMap<>();

    public void addReview(Review review) {
        if (review == null) {
            throw new IllegalArgumentException("Review cannot be null!");
        }
        // Adds to map of Item
        reviewsByItem.putIfAbsent(review.getItem(), new HashSet<>());
        reviewsByItem.get(review.getItem()).add(review);
        // Adds to map of User
        reviewsByUser.putIfAbsent(review.getUser(), new HashSet<>());
        reviewsByUser.get(review.getUser()).add(review);
    }

    public Set<Review> getReviewsByUser(User user) {
        return reviewsByUser.getOrDefault(user, new HashSet<>());
    }

    public Set<Review> getReviewsByItem(Item item) {
        return reviewsByItem.getOrDefault(item, new HashSet<>());
    }

    public double getAverageRating(Item item) {
        Set<Review> reviews = getReviewsByItem(item);

        if (reviews.isEmpty()) {
            throw new IllegalArgumentException("No reviews available for this item.");
        }

        int totalRating = 0;
        for (Review review : reviews) {
            totalRating += review.getRating();
        }
        return (float) totalRating / getReviewsByItem(item).size();
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(reviewsByUser.keySet());
    }

    public List<Item> getItemsByUser(User user) {
        List<Item> items = new ArrayList<>();
        for (Review review : getReviewsByUser(user)) {
            items.add(review.getItem());
        }
        return new ArrayList<>(items);
    }
}
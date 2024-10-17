package org.bibblan.reviews;

import org.bibblan.bookcatalog.domain.Item;
import org.bibblan.usermanagement.User;

import java.util.*;

public class ReviewCollection {

    private final Map<Item, Set<Review>> reviewsByItem = new HashMap<>();
    private final Map<User, Set<Review>> reviewsByUser = new HashMap<>();

    public void addReview(Review review){
        // Adds to map of Item
        reviewsByItem.putIfAbsent(review.getItem(), new HashSet<>());
        reviewsByItem.get(review.getItem()).add(review);
        // Adds to map of User
        reviewsByUser.putIfAbsent(review.getUser(), new HashSet<>());
        reviewsByUser.get(review.getUser()).add(review);
    }

    public boolean containsReview(Review review){
        return reviewsByItem.get(review.getItem()).contains(review);
    }

    public Set<Review> getReviewsByUser(User user){
        return reviewsByUser.get(user);
    }

    public Set<Review> getReviewsByItem(Item Item){
        return reviewsByItem.get(Item);
    }
}
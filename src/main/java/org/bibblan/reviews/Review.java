package org.bibblan.reviews;

import lombok.Value;
import org.bibblan.bookcatalog.domain.Item;
import org.bibblan.usermanagement.User;

@Value
public class Review {

    Item item;
    int rating;
    String comment;
    User user;

    public Review(Item item, int rating, User user) {
        this(item, rating, user, "");
    }

    public Review(Item item, int rating, User user, String comment) {
        validateitem(item);
        validateUser(user);
        validateRating(rating);
        validateComment(comment);

        this.item = item;
        this.rating = rating;
        this.comment = comment;
        this.user = user;
    }

    private static void validateComment(String comment) {
        if (comment.length() > 500) {
            throw new IllegalArgumentException("Comment must be between 1 and 500 characters!");
        }
    }

    private static void validateRating(int rating) {
        if (rating > 5 || rating < 1) {
            throw new IllegalArgumentException("Rating must be between 1 and 5!");
        }
    }

    private static void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null!");
        }
    }

    private static void validateitem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("item cannot be null!");
        }
    }
}
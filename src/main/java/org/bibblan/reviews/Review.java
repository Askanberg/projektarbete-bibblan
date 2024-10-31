package org.bibblan.reviews;

import lombok.Value;
import org.bibblan.bookcatalog.domain.Item;
import org.bibblan.usermanagement.user.*;

@Value
public class Review {

    private static int idCounter = 0;

    int id;
    Item item;
    int rating;
    String comment;
    User user;

    public Review(Item item, int rating, User user) {
        this(item, rating, user, "");
    }

    public Review(Item item, int rating, User user, String comment) {
        this.id = generateId();
        validateItem(item);
        validateUser(user);
        validateRating(rating);
        validateComment(comment);

        this.item = item;
        this.rating = rating;
        this.comment = comment;
        this.user = user;
    }

    private static int generateId() {
        idCounter++;
        return idCounter;
    }

    // Metod för att kunna ställa om countern för testning
    public static void resetIdCounter() {
        idCounter = 0; // Reset the counter to 0
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

    private static void validateItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("item cannot be null!");
        }
    }
}
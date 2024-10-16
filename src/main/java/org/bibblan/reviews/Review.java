package org.bibblan.reviews;

import lombok.Value;
import org.bibblan.bookcatalog.domain.Book;
import org.bibblan.usermanagement.User;

@Value
public class Review {

    Book book;
    int rating;
    String comment;
    User user;

    public Review(Book book, int rating, User user) {
        this(book, rating, user, "");
    }

    public Review(Book book, int rating, User user, String comment) {
        validateBook(book);
        validateUser(user);
        validateRating(rating);
        validateComment(comment);

        this.book = book;
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

    private static void validateBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null!");
        }
    }
}
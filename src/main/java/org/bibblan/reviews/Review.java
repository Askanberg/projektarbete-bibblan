package org.bibblan.reviews;

import lombok.Value;
import org.bibblan.bookcatalog.Book;
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
        this.book = book;
        this.rating = rating;
        this.comment = comment;
        this.user = user;
    }
}
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
        if(rating > 5  || rating < 1){
            throw new IllegalArgumentException("Rating must be between 1 and 5!");
        } else {
            this.rating = rating;
        }
        this.comment = comment;
        this.user = user;
    }
}
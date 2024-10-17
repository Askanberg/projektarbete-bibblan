package org.bibblan.reviews;

import org.bibblan.bookcatalog.domain.Book;
import org.bibblan.usermanagement.User;

import java.util.*;

public class ReviewCollection {

    private final Map<Book, Set<Review>> reviewsByBook = new HashMap<>();
    private final Map<User, Set<Review>> reviewsByUser = new HashMap<>();

    public void addReview(Review review){
        // Adds to map of Book
        reviewsByBook.putIfAbsent(review.getBook(), new HashSet<>());
        reviewsByBook.get(review.getBook()).add(review);
        // Adds to map of User
        reviewsByUser.putIfAbsent(review.getUser(), new HashSet<>());
        reviewsByUser.get(review.getUser()).add(review);
    }

    public boolean containsReview(Review review){
        return reviewsByBook.get(review.getBook()).contains(review);
    }

    public Set<Review> getReviewsByUser(User user){
        return reviewsByUser.get(user);
    }

    public Set<Review> getReviewsByBook(Book book){
        return reviewsByBook.get(book);
    }
}
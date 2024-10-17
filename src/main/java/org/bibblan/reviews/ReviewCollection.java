package org.bibblan.reviews;

import org.bibblan.bookcatalog.domain.Book;
import java.util.*;

public class ReviewCollection {

    private final Map<Book, Set<Review>> reviews = new HashMap<>();

    public void addReview(Review review){
        reviews.putIfAbsent(review.getBook(), new HashSet<>());
        reviews.get(review.getBook()).add(review);
    }

    public boolean containsReview(Review review){
        return reviews.get(review.getBook()).contains(review);
    }
}
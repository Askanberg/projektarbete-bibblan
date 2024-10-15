package org.bibblan.bookcatalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Book extends Item{

    private String isbn;
    private Author author;

    private CoverType coverType;

    private boolean isAvailable = true;

    public Book(String title, Author author, String genre, String isbn, String publisher, CoverType cover) {
        super(title, genre, publisher);
        this.author = author;
        this.isbn = isbn;
        this.coverType = cover;
    }

    @Override
    public String getArticleType() {
        return "Book";
    }
}

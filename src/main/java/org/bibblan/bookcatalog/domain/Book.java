package org.bibblan.bookcatalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class Book extends Item{

    private String isbn;
    private CoverType coverType;

    private boolean isAvailable = true;

    public Book(String title, Author author, String genre, String publisher, String isbn, CoverType cover) {
        super(title, genre, author, publisher);
        this.isbn = isbn;
        this.coverType = cover;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Book book)) return false;

        return isbn.equals(book.isbn) &&
                coverType == book.coverType &&
                super.getTitle().equals(book.getTitle()) &&
                super.getAuthor().equals(book.getAuthor()) &&
                super.getGenre().equals(book.getGenre()) &&
                super.getPublisher().equals(book.getPublisher());
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn, coverType, super.getTitle(), super.getAuthor(), super.getGenre(), super.getPublisher());
    }
}

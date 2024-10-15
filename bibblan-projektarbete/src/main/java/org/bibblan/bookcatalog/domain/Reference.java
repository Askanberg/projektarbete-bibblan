package org.bibblan.bookcatalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Reference extends Item{

    private String isbn;
    private Author author;

    private final CoverType coverType = CoverType.HARDCOVER;

    private final boolean isAvailable = false;

    public Reference(String title, Author author, String genre, String isbn, String publisher) {
        super(title, genre, publisher);
        this.author = author;
        this.isbn = isbn;
    }

    @Override
    public String getArticleType() {
        return "Reference";
    }
}

package org.bibblan.bookcatalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Reference extends Item{

    private String isbn;
    private final CoverType coverType = CoverType.HARDCOVER;

    private final boolean isAvailable = false;

    public Reference(String title, Author author, String genre, String publisher, String isbn) {
        super(title, genre, author, publisher);
        this.isbn = isbn;
    }

}

package org.bibblan.bookcatalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Book extends Article{

    private String isbn;

    private CoverType coverType;

    public Book(String title, String publisher, String publishedYear, ArrayList<Author> authors, String isbn, CoverType cover) {
        super(title, publisher, publishedYear, authors);
        this.isbn = isbn;
        this.coverType = cover;
    }

    @Override
    public String getArticleType() {
        return "Book";
    }
}

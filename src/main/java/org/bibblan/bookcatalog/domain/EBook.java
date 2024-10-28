package org.bibblan.bookcatalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class EBook extends Item{

    private String url;
    private String fileFormat;

    public EBook(String title, Author author, String genre, String publisher, String url, String fileFormat) {
        super(title, genre, author, publisher);
        this.url = url;
        this.fileFormat = fileFormat;
    }

}

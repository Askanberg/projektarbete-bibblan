package org.bibblan.bookcatalog;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;


public class BookTest {

    @Test
    void TestThatGetArticleTypeReturnsCorrectArticle() {
        Author author = new Author();
        Article book = new Book("Title", author, "Genre", "000-00-00-0-000", "Publisher", CoverType.HARDCOVER);

        String articleType = book.getArticleType();

        assertEquals("Book", articleType);
    }
}

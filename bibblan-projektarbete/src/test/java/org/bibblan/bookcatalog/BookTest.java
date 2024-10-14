package org.bibblan.bookcatalog;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;


public class BookTest {

    @Test
    void TestThatGetArticleTypeReturnsCorrectArticle() {
        Article book = new Book("Title", "Publisher", "2024-10-5", new ArrayList<Author>(), "000-00-00-0-000", CoverType.HARDCOVER);

        String articleType = book.getArticleType();

        assertEquals("Book", articleType);
    }
}

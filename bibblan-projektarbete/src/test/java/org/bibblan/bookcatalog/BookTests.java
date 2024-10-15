package org.bibblan.bookcatalog;

import org.bibblan.GeneralTestData;
import org.bibblan.bookcatalog.domain.Book;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class BookTests {
    private GeneralTestData testData = new GeneralTestData();

    @Test
    void TestThatGetArticleTypeReturnsCorrectArticle() {
        Book book = testData.createTestBookA();

        String articleType = book.getArticleType();

        assertEquals("Book", articleType);
    }
}

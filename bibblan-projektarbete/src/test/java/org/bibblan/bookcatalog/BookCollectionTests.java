package org.bibblan.bookcatalog;

import org.bibblan.GeneralTestData;
import org.bibblan.bookcatalog.domain.Book;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BookCollectionTests {

    @Test
    void testReadBooksFromCsv() {
        BookCollection tempCollection = new BookCollection();
        Map<String, Book> books = tempCollection.readBooksFromCsv("src/test/resources/testBooks.csv");

        assertTrue(books.containsKey("9781400052172"));
    }
}

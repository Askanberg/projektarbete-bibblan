package org.bibblan.bookcatalog;

import org.bibblan.GeneralTestData;
import org.bibblan.bookcatalog.domain.Book;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BookCollectionTests {

    @Test
    void testReadBooksFromCsv() throws FileNotFoundException {
        BookCollection tempCollection = new BookCollection();
        Map<String, Book> books = tempCollection.readBooksFromCsv("src/test/resources/testBooks.csv");
        assertTrue(books.containsKey("9781400052172"));
    }

    @Test
    void testThatReadBooksFromCsvThrowsExceptionIfNoSuchFile() {
        BookCollection tempCollection = new BookCollection();
        assertThrows(FileNotFoundException.class, () -> {
            tempCollection.readBooksFromCsv("src/test/resources/NoSuchFile.csv");
        });
    }

    @Test
    void testThatReadBooksFromCsvThrowsExceptionIfFileIsEmpty() {
        BookCollection tempCollection = new BookCollection();
        assertThrows(IllegalArgumentException.class, () -> {
            Map<String, Book> books = tempCollection.readBooksFromCsv("src/test/resources/emptyBookFile.csv");
        });
    }

    @Test
    void testThatMakeBookThrowsExceptionIfNumberOfColumnsIsInvalid() {
        BookCollection tempCollection = new BookCollection();
        assertThrows(IllegalArgumentException.class, () -> {
            Map<String, Book> books = tempCollection.readBooksFromCsv("src/test/resources/invalidColumnsBooks.csv");
        });
    }
}

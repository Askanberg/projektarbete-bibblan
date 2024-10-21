package org.bibblan.bookcatalog;

import org.bibblan.bookcatalog.domain.Author;
import org.bibblan.bookcatalog.domain.Book;
import org.bibblan.bookcatalog.domain.CoverType;
import org.bibblan.bookcatalog.domain.EBook;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ItemCollectionTests {

    @Test
    void testThatReadItemsFromCsvCanReadBookCsv() throws FileNotFoundException {
        ItemCollection tempCollection = new ItemCollection();
        tempCollection.readItemsFromCsv("src/test/resources/testBooks.csv");
        Book book = new Book("Educated", new Author("Tara Westover", new ArrayList<>()), "Biography", "Random House", "9780399590504", CoverType.POCKET);
        assertEquals(book, tempCollection.getItemMap().get("9780399590504"));
    }

    @Test
    void testThatReadItemsFromCsvThrowsExceptionIfFileNotFound() {
        ItemCollection tempCollection = new ItemCollection();
        assertThrows(FileNotFoundException.class, () -> {
            tempCollection.readItemsFromCsv("src/test/resources/NoSuchFile.csv");
        });
    }

    @Test
    void testThatReadItemsFromCsvThrowsExceptionIfFileIsEmpty() {
        ItemCollection tempCollection = new ItemCollection();
        assertThrows(IllegalArgumentException.class, () -> {
            tempCollection.readItemsFromCsv("src/test/resources/emptyFile.csv");
        });
    }

    @Test
    void testThatReadItemsFromCsvThrowsExceptionForInvalidItemType() {
        ItemCollection tempCollection = new ItemCollection();
        assertThrows(IllegalArgumentException.class, () -> {
            tempCollection.readItemsFromCsv("src/test/resources/mixedItemTypes.csv");
        });
    }

    @Test
    void testThatReadItemsFromCsvCanReadDigitalBookCsv() throws FileNotFoundException {
        ItemCollection tempCollection = new ItemCollection();
        tempCollection.readItemsFromCsv("src/test/resources/testEBooks.csv");
        EBook eBook = new EBook("Effective Java", new Author("Joshua Bloch", new ArrayList<>()), "Programming", "Addison-Wesley", "http://example.com/effective_java", "EPUB");
        assertEquals(eBook, tempCollection.getItemMap().get("http://example.com/effective_java"));
    }
}
